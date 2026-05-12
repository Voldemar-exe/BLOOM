package com.example.data.repository

import com.example.data.model.SyncQueue
import com.example.data.util.toDomain
import com.example.data.util.toDto
import com.example.data.util.toEntity
import com.example.database.dao.SyncDao
import com.example.database.dao.SyncQueueDao
import com.example.database.model.SyncTypes
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskEntity
import com.example.network.api.SyncApi
import com.example.network.model.HabitCompletionDto
import com.example.network.model.HabitDto
import com.example.network.model.HabitReminderDto
import com.example.network.model.StatsLogDto
import com.example.network.model.SyncPullResponse
import com.example.network.model.SyncPushRequest
import com.example.network.model.TaskCompletionDto
import com.example.network.model.TaskDto
import com.example.network.model.TaskReminderDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

interface SyncRepository {
    fun observePending(): Flow<List<SyncQueue>>

    suspend fun deleteByIds(ids: List<Long>)

    suspend fun insert(entity: SyncQueue)

    suspend fun pushChanges(): Result<Unit>

    suspend fun pullChanges(lastSyncTimestamp: Long): Result<SyncPullResponse>
}

class SyncRepositoryImpl(
    private val dao: SyncQueueDao,
    private val syncDao: SyncDao,
    private val api: SyncApi,
) : SyncRepository {
    override fun observePending(): Flow<List<SyncQueue>> =
        dao.observeSyncQueue().map { entities -> entities.map { it.toDomain() } }

    override suspend fun deleteByIds(ids: List<Long>) {
        dao.deleteByIds(ids)
    }

    override suspend fun insert(entity: SyncQueue) {
        dao.insert(entity.toEntity())
    }

    // TODO: Optimize this
    override suspend fun pushChanges(): Result<Unit> =
        withContext(Dispatchers.IO) {
            val pendingEntities = dao.getPendingList()
            if (pendingEntities.isEmpty()) return@withContext Result.success(Unit)

            val pending = pendingEntities.map { it.toDomain() }

            val habits = mutableListOf<HabitDto>()
            val tasks = mutableListOf<TaskDto>()
            val habitReminders = mutableListOf<HabitReminderDto>()
            val taskReminders = mutableListOf<TaskReminderDto>()
            val statsLogs = mutableListOf<StatsLogDto>()
            val habitCompletions = mutableListOf<HabitCompletionDto>()
            val taskCompletions = mutableListOf<TaskCompletionDto>()
            val processedIds = mutableListOf<Long>()

            for (item in pending) {
                when (item.entityType) {
                    SyncTypes.HABIT ->
                        syncDao
                            .getHabitById(item.entityId)
                            ?.toDto(syncDao.getHabitPlantByHabitId(item.entityId)!!)
                            ?.let {
                                habits += it
                                processedIds += item.id
                            }

                    SyncTypes.TASK ->
                        syncDao
                            .getTaskById(item.entityId)
                            ?.toDto(syncDao.getSubtaskByTaskId(item.entityId))
                            ?.let {
                                tasks += it
                                processedIds += item.id
                            }

                    SyncTypes.HABIT_REMINDER ->
                        syncDao
                            .getHabitReminderById(item.entityId)
                            ?.toDto()
                            ?.let {
                                habitReminders += it
                                processedIds += item.id
                            }

                    SyncTypes.TASK_REMINDER ->
                        syncDao
                            .getTaskReminderById(item.entityId)
                            ?.toDto()
                            ?.let {
                                taskReminders += it
                                processedIds += item.id
                            }

                    SyncTypes.STATS_LOG ->
                        syncDao
                            .getStatsLogById(item.entityId)
                            ?.toDto()
                            ?.let {
                                statsLogs += it
                                processedIds += item.id
                            }

                    SyncTypes.HABIT_COMPLETION ->
                        syncDao
                            .getHabitCompletionById(item.entityId)
                            ?.toDto()
                            ?.let {
                                habitCompletions += it
                                processedIds += item.id
                            }

                    SyncTypes.TASK_COMPLETION ->
                        syncDao
                            .getTaskCompletionById(item.entityId)
                            ?.toDto()
                            ?.let {
                                taskCompletions += it
                                processedIds += item.id
                            }

                    SyncTypes.SUBTASK -> { /* they stored in TASK */ }

                    else -> Timber.w("Unknown sync entity type: ${item.entityType}")
                }
            }

            if (processedIds.isEmpty()) return@withContext Result.success(Unit)

            val request =
                SyncPushRequest(
                    habits = habits,
                    tasks = tasks,
                    habitReminders = habitReminders,
                    taskReminders = taskReminders,
                    statsLogs = statsLogs,
                    habitCompletions = habitCompletions,
                    taskCompletions = taskCompletions,
                    lastSyncTimestamp = pending.maxOfOrNull { it.createdAt } ?: 0L,
                )

            return@withContext runCatching {
                api.push(request)
                dao.deleteByIds(processedIds)
            }.onFailure {
                Timber.e(it, "Sync push failed for ${processedIds.size} items")
            }
        }

    override suspend fun pullChanges(lastSyncTimestamp: Long): Result<SyncPullResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response =
                    api.pull(lastSyncTimestamp).getOrElse {
                        return@withContext Result.failure(it)
                    }

                Timber.d("$response")

                val habitTsMap =
                    syncDao
                        .getHabitsTimestamps(response.habits.map { it.id })
                        .associateBy { it.id }
                val habitsToUpsert = mutableListOf<HabitEntity>()
                val plantsToUpsert = mutableListOf<HabitPlantEntity>()
                response.habits.forEach { dto ->
                    val local = habitTsMap[dto.id]
                    if (local == null || dto.updatedAt > local.updatedAt) {
                        habitsToUpsert.add(dto.toEntity())
                        plantsToUpsert.add(dto.plantDto.toEntity())
                    }
                }
                if (habitsToUpsert.isNotEmpty()) syncDao.upsertHabits(habitsToUpsert)
                if (plantsToUpsert.isNotEmpty()) syncDao.upsertHabitPlants(plantsToUpsert)

                val taskTsMap =
                    syncDao
                        .getTasksTimestamps(response.tasks.map { it.id })
                        .associateBy { it.id }
                val tasksToUpsert = mutableListOf<TaskEntity>()
                val subtasksToUpsert = mutableListOf<SubtaskEntity>()
                response.tasks.forEach { dto ->
                    val local = taskTsMap[dto.id]
                    if (local == null || dto.updatedAt > local.updatedAt) {
                        tasksToUpsert.add(dto.toEntity())
                        subtasksToUpsert.addAll(dto.subtasks.map { it.toEntity() })
                    }
                }
                if (tasksToUpsert.isNotEmpty()) syncDao.upsertTasks(tasksToUpsert)
                if (subtasksToUpsert.isNotEmpty()) syncDao.upsertSubtasks(subtasksToUpsert)

                val hRemTsMap =
                    syncDao
                        .getHabitRemindersTimestamps(response.habitReminders.map { it.id })
                        .associateBy { it.id }
                val hRemsToUpsert =
                    response.habitReminders
                        .filter { dto ->
                            val local = hRemTsMap[dto.id]
                            local == null || dto.updatedAt > local.updatedAt
                        }.map { it.toEntity() }
                if (hRemsToUpsert.isNotEmpty()) syncDao.upsertHabitReminders(hRemsToUpsert)

                val tRemTsMap =
                    syncDao
                        .getTaskRemindersTimestamps(response.taskReminders.map { it.id })
                        .associateBy { it.id }
                val tRemsToUpsert =
                    response.taskReminders
                        .filter { dto ->
                            val local = tRemTsMap[dto.id]
                            local == null || dto.updatedAt > local.updatedAt
                        }.map { it.toEntity() }
                if (tRemsToUpsert.isNotEmpty()) syncDao.upsertTaskReminders(tRemsToUpsert)

                syncDao.upsertStatsLogs(response.statsLogs.map { it.toEntity() })
                syncDao.upsertHabitCompletions(response.habitCompletions.map { it.toEntity() })
                syncDao.upsertTaskCompletions(response.taskCompletions.map { it.toEntity() })

                Result.success(response)
            } catch (e: Exception) {
                Timber.e(e, "Sync pull failed at timestamp $lastSyncTimestamp")
                Result.failure(e)
            }
        }
}
