package com.example.data.repository

import com.example.data.model.SyncQueue
import com.example.data.util.toDomain
import com.example.data.util.toDto
import com.example.data.util.toEntity
import com.example.data.util.toModel
import com.example.database.dao.SyncDao
import com.example.database.dao.SyncQueueDao
import com.example.database.model.SyncTimestamp
import com.example.database.model.SyncTypes
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskEntity
import com.example.database.util.TransactionRunner
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.api.SyncApi
import com.example.network.model.AppSettingsDto
import com.example.network.model.HabitCompletionDto
import com.example.network.model.HabitDto
import com.example.network.model.HabitReminderDto
import com.example.network.model.StatsLogDto
import com.example.network.model.SyncPullResponse
import com.example.network.model.SyncPushRequest
import com.example.network.model.TaskCompletionDto
import com.example.network.model.TaskDto
import com.example.network.model.TaskReminderDto
import com.example.network.model.UserProfileDto
import com.example.network.model.UserStatsDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber

interface SyncRepository {
    fun observePending(): Flow<List<SyncQueue>>

    suspend fun deleteByIds(ids: List<Long>)

    suspend fun insert(syncQueue: SyncQueue)

    suspend fun pushChanges(lastSyncTimestamp: Long): Result<Unit>

    suspend fun pullChanges(lastSyncTimestamp: Long): Result<SyncPullResponse>
}

class SyncRepositoryImpl(
    private val syncQueueDao: SyncQueueDao,
    private val syncDao: SyncDao,
    private val dataStore: BloomPreferencesDataStore,
    private val api: SyncApi,
    private val transactionRunner: TransactionRunner,
) : SyncRepository {
    override fun observePending(): Flow<List<SyncQueue>> =
        syncQueueDao.observeSyncQueue().map { entities -> entities.map { it.toDomain() } }

    override suspend fun deleteByIds(ids: List<Long>) {
        syncQueueDao.deleteByIds(ids)
    }

    override suspend fun insert(syncQueue: SyncQueue) {
        syncQueueDao.insert(syncQueue.toEntity())
    }

    override suspend fun pushChanges(lastSyncTimestamp: Long): Result<Unit> {
        val pendingEntities = syncQueueDao.getPendingList()
        if (pendingEntities.isEmpty()) return Result.success(Unit)
        if (dataStore.user.first() == null) return Result.success(Unit)

        val pending = pendingEntities.map { it.toDomain() }
        val collection = collectPushData(pending, lastSyncTimestamp)

        if (collection.processedIds.isEmpty()) {
            return Result.success(Unit)
        }

        return runCatching {
            api.push(collection.request).getOrThrow()
            syncQueueDao.deleteByIds(collection.processedIds)
        }.onFailure {
            Timber.e(it, "Sync push failed for ${collection.processedIds.size} items")
        }
    }

    override suspend fun pullChanges(lastSyncTimestamp: Long): Result<SyncPullResponse> =
        runCatching {
            val response = api.pull(lastSyncTimestamp).getOrThrow()
            applyPullResponse(response)
            response
        }.onFailure {
            Timber.e(it, "Sync pull failed at timestamp $lastSyncTimestamp")
        }

    private suspend fun collectPushData(
        pending: List<SyncQueue>,
        lastSyncTimestamp: Long,
    ): PushCollection {
        val habits = mutableListOf<HabitDto>()
        val tasks = mutableListOf<TaskDto>()
        val habitReminders = mutableListOf<HabitReminderDto>()
        val taskReminders = mutableListOf<TaskReminderDto>()
        val statsLogs = mutableListOf<StatsLogDto>()
        val habitCompletions = mutableListOf<HabitCompletionDto>()
        val taskCompletions = mutableListOf<TaskCompletionDto>()
        val userProfile: UserProfileDto? = dataStore.user.first()?.toDto(lastSyncTimestamp)
        val userStats: UserStatsDto = dataStore.stats.first().toDto(lastSyncTimestamp)
        val appSettings: AppSettingsDto = dataStore.settings.first().toDto(lastSyncTimestamp)
        val processedIds = mutableListOf<Long>()

        pending.forEach { item ->
            when (item.entityType) {
                SyncTypes.HABIT -> {
                    val habit = syncDao.getHabitById(item.entityId)
                    val plant = syncDao.getHabitPlantByHabitId(item.entityId)

                    if (habit != null && plant != null) {
                        habits += habit.toDto(plant)
                        processedIds += item.id
                    }
                }

                SyncTypes.TASK -> {
                    val task = syncDao.getTaskById(item.entityId)
                    if (task != null) {
                        val subtasks = syncDao.getSubtaskByTaskId(item.entityId)
                        tasks += task.toDto(subtasks)
                        processedIds += item.id
                    }
                }

                SyncTypes.HABIT_REMINDER -> {
                    syncDao
                        .getHabitReminderById(item.entityId)
                        ?.toDto()
                        ?.let {
                            habitReminders += it
                            processedIds += item.id
                        }
                }

                SyncTypes.TASK_REMINDER -> {
                    syncDao
                        .getTaskReminderById(item.entityId)
                        ?.toDto()
                        ?.let {
                            taskReminders += it
                            processedIds += item.id
                        }
                }

                SyncTypes.STATS_LOG -> {
                    syncDao
                        .getStatsLogById(item.entityId)
                        ?.toDto()
                        ?.let {
                            statsLogs += it
                            processedIds += item.id
                        }
                }

                SyncTypes.HABIT_COMPLETION -> {
                    syncDao
                        .getHabitCompletionById(item.entityId)
                        ?.toDto()
                        ?.let {
                            habitCompletions += it
                            processedIds += item.id
                        }
                }

                SyncTypes.TASK_COMPLETION -> {
                    syncDao
                        .getTaskCompletionById(item.entityId)
                        ?.toDto()
                        ?.let {
                            taskCompletions += it
                            processedIds += item.id
                        }
                }

                SyncTypes.SUBTASK -> Unit

                else -> {
                    Timber.w("Unknown sync entity type: ${item.entityType}")
                    processedIds += item.id
                }
            }
        }

        val processedItems = pending.filter { it.id in processedIds }

        return PushCollection(
            request =
                SyncPushRequest(
                    habits = habits,
                    tasks = tasks,
                    habitReminders = habitReminders,
                    taskReminders = taskReminders,
                    statsLogs = statsLogs,
                    habitCompletions = habitCompletions,
                    taskCompletions = taskCompletions,
                    user = userProfile,
                    userStats = userStats,
                    appSettings = appSettings,
                    lastSyncTimestamp = processedItems.maxOfOrNull { it.createdAt } ?: 0L,
                ),
            processedIds = processedIds,
        )
    }

    private suspend fun applyPullResponse(response: SyncPullResponse) {
        transactionRunner.run {
            val habitTsMap =
                syncDao
                    .getHabitsTimestamps(response.habits.map { it.id })
                    .associateBy { it.id }

            val habitsToUpsert = mutableListOf<HabitEntity>()
            val plantsToUpsert = mutableListOf<HabitPlantEntity>()

            filterNewer(
                remote = response.habits,
                localMap = habitTsMap,
                idSelector = { it.id },
                updatedAtSelector = { it.updatedAt },
            ).forEach { dto ->
                habitsToUpsert += dto.toEntity()
                plantsToUpsert += dto.plantDto.toEntity()
            }

            if (habitsToUpsert.isNotEmpty()) syncDao.upsertHabits(habitsToUpsert)
            if (plantsToUpsert.isNotEmpty()) syncDao.upsertHabitPlants(plantsToUpsert)

            val taskTsMap =
                syncDao
                    .getTasksTimestamps(response.tasks.map { it.id })
                    .associateBy { it.id }

            val tasksToUpsert = mutableListOf<TaskEntity>()
            val subtasksToUpsert = mutableListOf<SubtaskEntity>()

            filterNewer(
                remote = response.tasks,
                localMap = taskTsMap,
                idSelector = { it.id },
                updatedAtSelector = { it.updatedAt },
            ).forEach { dto ->
                tasksToUpsert += dto.toEntity()
                subtasksToUpsert += dto.subtasks.map { it.toEntity() }
            }

            if (tasksToUpsert.isNotEmpty()) syncDao.upsertTasks(tasksToUpsert)
            if (subtasksToUpsert.isNotEmpty()) syncDao.upsertSubtasks(subtasksToUpsert)

            val habitReminderTsMap =
                syncDao
                    .getHabitRemindersTimestamps(response.habitReminders.map { it.id })
                    .associateBy { it.id }

            val habitRemindersToUpsert =
                filterNewer(
                    remote = response.habitReminders,
                    localMap = habitReminderTsMap,
                    idSelector = { it.id },
                    updatedAtSelector = { it.updatedAt },
                ).map { it.toEntity() }

            if (habitRemindersToUpsert.isNotEmpty()) {
                syncDao.upsertHabitReminders(habitRemindersToUpsert)
            }

            val taskReminderTsMap =
                syncDao
                    .getTaskRemindersTimestamps(response.taskReminders.map { it.id })
                    .associateBy { it.id }

            val taskRemindersToUpsert =
                filterNewer(
                    remote = response.taskReminders,
                    localMap = taskReminderTsMap,
                    idSelector = { it.id },
                    updatedAtSelector = { it.updatedAt },
                ).map { it.toEntity() }

            if (taskRemindersToUpsert.isNotEmpty()) {
                syncDao.upsertTaskReminders(taskRemindersToUpsert)
            }

            syncDao.upsertStatsLogs(response.statsLogs.map { it.toEntity() })
            syncDao.upsertHabitCompletions(response.habitCompletions.map { it.toEntity() })
            syncDao.upsertTaskCompletions(response.taskCompletions.map { it.toEntity() })

            response.user?.let { dataStore.setUser(it.toModel()) }
            response.userStats?.let { dataStore.setStats(it.toModel()) }
            response.appSettings?.let { dataStore.setSettings(it.toModel()) }
        }
    }

    private inline fun <T, K> filterNewer(
        remote: List<T>,
        localMap: Map<K, SyncTimestamp>,
        idSelector: (T) -> K,
        updatedAtSelector: (T) -> Long,
    ): List<T> =
        remote.filter { item ->
            val local = localMap[idSelector(item)]
            local == null || updatedAtSelector(item) > local.updatedAt
        }

    private data class PushCollection(val request: SyncPushRequest, val processedIds: List<Long>)
}
