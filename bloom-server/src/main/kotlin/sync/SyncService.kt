package sync

import com.example.db.daos.*
import com.example.db.tables.*
import com.example.model.*
import com.example.utils.toDto
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import utils.syncEntities

interface SyncService {
    suspend fun push(
        userId: Long,
        request: SyncPushRequest,
    ): Result<Unit>

    suspend fun pull(
        userId: Long,
        lastSyncTimestamp: Long,
    ): SyncPullResponse
}

class SyncServiceImpl : SyncService {
    override suspend fun push(
        userId: Long,
        request: SyncPushRequest,
    ): Result<Unit> =
        transaction {
            syncHabits(userId, request.habits)
            syncTasks(userId, request.tasks)
            syncHabitReminders(userId, request.habitReminders)
            syncTaskReminders(userId, request.taskReminders)
            syncStatsLogs(userId, request.statsLogs)
            syncHabitCompletions(userId, request.habitCompletions)
            syncTaskCompletions(userId, request.taskCompletions)
            Result.success(Unit)
        }

    override suspend fun pull(
        userId: Long,
        lastSyncTimestamp: Long,
    ): SyncPullResponse =
        transaction {
            val serverTimestamp = System.currentTimeMillis()
            SyncPullResponse(
                habits =
                    HabitDAO
                        .findUpdatedSince(userId, lastSyncTimestamp)
                        .map { it.toDto() },
                tasks =
                    TaskDAO
                        .findUpdatedSince(userId, lastSyncTimestamp)
                        .map { it.toDto() },
                habitReminders =
                    HabitReminderDAO
                        .findUpdatedSince(userId, lastSyncTimestamp)
                        .map { it.toDto() },
                taskReminders =
                    TaskReminderDAO
                        .findUpdatedSince(userId, lastSyncTimestamp)
                        .map { it.toDto() },
                statsLogs =
                    StatsLogDAO
                        .findCreatedSince(userId, lastSyncTimestamp)
                        .map { it.toDto() },
                habitCompletions =
                    HabitCompletionDAO
                        .findCreatedSince(
                            userId,
                            lastSyncTimestamp,
                        ).map { it.toDto() },
                taskCompletions =
                    TaskCompletionDAO
                        .findCreatedSince(
                            userId,
                            lastSyncTimestamp,
                        ).map { it.toDto() },
                serverTimestamp = serverTimestamp,
            )
        }

    private fun syncHabits(
        userId: Long,
        dtos: List<HabitDto>,
    ) {
        syncEntities(
            incoming = dtos,
            extractId = { it.id },
            loadExisting = { ids ->
                HabitDAO
                    .find { (HabitsTable.userId eq userId) and (HabitsTable.id inList ids) }
                    .associateBy { it.id.value }
            },
            shouldUpdate = { dto, dao -> dto.updatedAt > dao.updatedAt },
            create = { dto ->
                HabitDAO.create(userId, dto).also { dao ->
                    dto.plantDto.takeIf { it.id > 0 }?.let { plant ->
                        HabitPlantDAO.create(userId, dao.id.value, plant)
                    }
                }
            },
            update = { dto, dao ->
                dao.updateFrom(dto)
                dto.plantDto.takeIf { it.id > 0 }?.let { plant ->
                    HabitPlantDAO
                        .findByIdAndHabit(plant.id, dao.id.value)
                        ?.updateFrom(plant)
                        ?: HabitPlantDAO.create(userId, dao.id.value, plant)
                }
            },
        )
    }

    private fun syncTasks(
        userId: Long,
        dtos: List<TaskDto>,
    ) {
        syncEntities(
            incoming = dtos,
            extractId = { it.id },
            loadExisting = { ids ->
                TaskDAO
                    .find { (TasksTable.userId eq userId) and (TasksTable.id inList ids) }
                    .associateBy { it.id.value }
            },
            shouldUpdate = { dto, dao -> dto.updatedAt > dao.updatedAt },
            create = { dto ->
                TaskDAO.create(userId, dto).also { dao ->
                    dto.subtasks.forEach { sub ->
                        SubtaskDAO.create(userId, dao.id.value, sub)
                    }
                }
            },
            update = { dto, dao ->
                dao.updateFrom(dto)
                dto.subtasks.forEach { sub ->
                    SubtaskDAO
                        .find { SubtasksTable.taskId eq dao.id.value }
                        .firstOrNull()
                        ?.updateFrom(sub)
                        ?: SubtaskDAO.create(userId, dao.id.value, sub)
                }
            },
        )
    }

    private fun syncHabitReminders(
        userId: Long,
        dtos: List<HabitReminderDto>,
    ) {
        syncEntities(
            incoming = dtos,
            extractId = { it.id },
            loadExisting = { ids ->
                HabitReminderDAO
                    .find {
                        (HabitRemindersTable.userId eq userId) and
                            (HabitRemindersTable.id inList ids)
                    }.associateBy { it.id.value }
            },
            shouldUpdate = { dto, dao -> dto.updatedAt > dao.updatedAt },
            create = { dto -> HabitReminderDAO.create(userId, dto.habitId, dto) },
            update = { dto, dao -> dao.updateFrom(dto) },
        )
    }

    private fun syncTaskReminders(
        userId: Long,
        dtos: List<TaskReminderDto>,
    ) {
        syncEntities(
            incoming = dtos,
            extractId = { it.id },
            loadExisting = { ids ->
                TaskReminderDAO
                    .find {
                        (TaskRemindersTable.userId eq userId) and
                            (TaskRemindersTable.id inList ids)
                    }.associateBy { it.id.value }
            },
            shouldUpdate = { dto, dao -> dto.updatedAt > dao.updatedAt },
            create = { dto -> TaskReminderDAO.create(userId, dto.taskId, dto) },
            update = { dto, dao -> dao.updateFrom(dto) },
        )
    }

    private fun syncStatsLogs(
        userId: Long,
        dtos: List<StatsLogDto>,
    ) {
        val existingEventIds =
            StatsLogDAO
                .find { StatsLogsTable.eventId inList dtos.map { it.eventId } }
                .map { it.eventId }
                .toSet()

        dtos.filter { it.eventId !in existingEventIds }.forEach { dto ->
            StatsLogDAO.create(userId, dto)
        }
    }

    private fun syncHabitCompletions(
        userId: Long,
        dtos: List<HabitCompletionDto>,
    ) {
        val existingIds =
            HabitCompletionDAO
                .find { HabitCompletionsTable.id inList dtos.map { it.id } }
                .map { it.id.value }
                .toSet()

        dtos.filter { it.id !in existingIds }.forEach { dto ->
            HabitCompletionDAO.create(userId, dto.habitId, dto)
        }
    }

    private fun syncTaskCompletions(
        userId: Long,
        dtos: List<TaskCompletionDto>,
    ) {
        val existingIds =
            TaskCompletionDAO
                .find { TaskCompletionsTable.id inList dtos.map { it.id } }
                .map { it.id.value }
                .toSet()

        dtos.filter { it.id !in existingIds }.forEach { dto ->
            TaskCompletionDAO.create(userId, dto.taskId, dto)
        }
    }
}

private fun HabitDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = find { (HabitsTable.userId eq userId) and (HabitsTable.updatedAt greater ts) }

private fun TaskDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = find { (TasksTable.userId eq userId) and (TasksTable.updatedAt greater ts) }

private fun HabitReminderDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = find { (HabitRemindersTable.userId eq userId) and (HabitRemindersTable.updatedAt greater ts) }

private fun TaskReminderDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = find { (TaskRemindersTable.userId eq userId) and (TaskRemindersTable.updatedAt greater ts) }

private fun StatsLogDAO.Companion.findCreatedSince(
    userId: Long,
    ts: Long,
) = find { (StatsLogsTable.userId eq userId) and (StatsLogsTable.createdAt greater ts) }

private fun HabitCompletionDAO.Companion.findCreatedSince(
    userId: Long,
    ts: Long,
) = find {
    (HabitCompletionsTable.userId eq userId) and (HabitCompletionsTable.createdAt greater ts)
}

private fun TaskCompletionDAO.Companion.findCreatedSince(
    userId: Long,
    ts: Long,
) = find { (TaskCompletionsTable.userId eq userId) and (TaskCompletionsTable.createdAt greater ts) }
