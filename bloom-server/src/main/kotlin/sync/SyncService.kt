package sync

import com.example.db.daos.*
import com.example.db.tables.*
import com.example.model.*
import com.example.utils.toDto
import com.example.utils.toProfileDto
import db.daos.UserAchievementDAO
import db.daos.UserCustomizationDAO
import db.tables.UserAchievementsTable
import db.tables.UserCustomizationsTable
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
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
        suspendTransaction {
            syncHabits(userId, request.habits)
            syncTasks(userId, request.tasks)
            syncHabitReminders(userId, request.habitReminders)
            syncTaskReminders(userId, request.taskReminders)
            syncStatsLogs(userId, request.statsLogs)
            syncHabitCompletions(userId, request.habitCompletions)
            syncTaskCompletions(userId, request.taskCompletions)

            syncUserProfile(userId, request.user)
            if (request.user != null) {
                syncUserAchievements(userId, request.user.ownedAchievements)
                syncUserCustomizations(userId, request.user.ownedItems)
            }
            syncUserStats(userId, request.userStats)
            syncAppSettings(userId, request.appSettings)

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
                user =
                    UserDAO
                        .findUpdatedSince(
                            userId,
                            lastSyncTimestamp,
                        )?.toProfileDto(
                            achievements =
                                UserAchievementDAO
                                    .findUpdatedSince(
                                        userId,
                                    ).map { it.achievementId }
                                    .toSet(),
                            items =
                                UserCustomizationDAO.findUpdatedSince(userId).map {
                                    CustomizationItemDto(
                                        key = it.key,
                                        type = it.type,
                                    )
                                },
                        ),
                userStats =
                    UserStatsDAO
                        .findUpdatedSince(
                            userId,
                            lastSyncTimestamp,
                        )?.toDto(),
                appSettings =
                    AppSettingsDAO
                        .findUpdatedSince(
                            userId,
                            lastSyncTimestamp,
                        )?.toDto(),
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
                    .find { (HabitsTable.userId eq userId) and (HabitsTable.localId inList ids) }
                    .associateBy { it.localId }
            },
            shouldUpdate = { dto, dao -> dto.updatedAt > dao.updatedAt },
            create = { dto ->
                HabitDAO.create(userId, dto).also { dao ->
                    dto.plantDto.takeIf { it.id > 0 }?.let { plant ->
                        HabitPlantDAO.create(dao.id, plant)
                    }
                }
            },
            update = { dto, dao ->
                dao.updateFrom(dto)
                dto.plantDto.takeIf { it.id > 0 }?.let { plant ->
                    HabitPlantDAO
                        .find { HabitPlantsTable.habitId eq dao.localId }
                        .firstOrNull()
                        ?.updateFrom(plant)
                        ?: HabitPlantDAO.create(dao.id, plant)
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
                    .find { (TasksTable.userId eq userId) and (TasksTable.localId inList ids) }
                    .associateBy { it.localId }
            },
            shouldUpdate = { dto, dao -> dto.updatedAt > dao.updatedAt },
            create = { dto ->
                TaskDAO.create(userId, dto).also { dao ->
                    dto.subtasks.forEach { sub ->
                        SubtaskDAO.create(dao.id, sub)
                    }
                }
            },
            update = { dto, dao ->
                dao.updateFrom(dto)
                dto.subtasks.forEach { sub ->
                    SubtaskDAO
                        .find { SubtasksTable.taskId eq dao.localId }
                        .firstOrNull()
                        ?.updateFrom(sub)
                        ?: SubtaskDAO.create(dao.id, sub)
                }
            },
        )
    }

    private fun syncHabitReminders(
        userId: Long,
        dtos: List<HabitReminderDto>,
    ) {
        if (dtos.isEmpty()) return

        val createdAtSet = dtos.map { it.createdAt }.toSet()

        val habitIds = dtos.map { it.habitId }.toSet()

        val habitsIds =
            HabitDAO
                .find {
                    (HabitsTable.localId inList habitIds) and
                        (HabitsTable.userId eq userId)
                }.associateBy { it.localId }

        val existing =
            HabitReminderDAO
                .wrapRows(
                    HabitRemindersTable
                        .innerJoin(HabitsTable)
                        .select(HabitRemindersTable.columns)
                        .where {
                            (HabitsTable.userId eq userId) and
                                (HabitRemindersTable.createdAt inList createdAtSet)
                        },
                ).map { it.habitId to it.createdAt }
                .toHashSet()

        dtos.forEach { dto ->
            val habit = habitsIds[dto.habitId] ?: return@forEach

            val key = habit.id to dto.createdAt
            if (key !in existing) {
                HabitReminderDAO.create(habit.id, dto)
            }
        }
    }

    private fun syncTaskReminders(
        userId: Long,
        dtos: List<TaskReminderDto>,
    ) {
        if (dtos.isEmpty()) return

        val createdAtSet = dtos.map { it.createdAt }.toSet()

        val taskIds = dtos.map { it.taskId }.toSet()

        val tasksIds =
            TaskDAO
                .find {
                    (TasksTable.localId inList taskIds) and
                        (TasksTable.userId eq userId)
                }.associateBy { it.localId }

        val existing =
            TaskReminderDAO
                .wrapRows(
                    TaskRemindersTable
                        .innerJoin(TasksTable)
                        .select(TaskRemindersTable.columns)
                        .where {
                            (TasksTable.userId eq userId) and
                                (TaskRemindersTable.createdAt inList createdAtSet)
                        },
                ).map { it.taskId to it.createdAt }
                .toHashSet()

        dtos.forEach { dto ->
            val task = tasksIds[dto.taskId] ?: return@forEach

            val key = task.id to dto.createdAt
            if (key !in existing) {
                TaskReminderDAO.create(task.id, dto)
            }
        }
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
        if (dtos.isEmpty()) return

        val habitIds = dtos.map { it.habitId }.toSet()
        val createdAtSet = dtos.map { it.createdAt }.toSet()

        val habitsIds =
            HabitDAO
                .find {
                    (HabitsTable.localId inList habitIds) and
                        (HabitsTable.userId eq userId)
                }.associateBy { it.localId }

        val existing =
            HabitCompletionDAO
                .wrapRows(
                    HabitCompletionsTable
                        .innerJoin(HabitsTable)
                        .select(HabitCompletionsTable.columns)
                        .where {
                            (HabitsTable.userId eq userId) and
                                (HabitCompletionsTable.createdAt inList createdAtSet)
                        },
                ).map { it.habitId to it.createdAt }
                .toHashSet()

        dtos.forEach { dto ->
            val habit = habitsIds[dto.habitId] ?: return@forEach

            val key = habit.id to dto.createdAt
            if (key !in existing) {
                HabitCompletionDAO.create(habit.id, dto)
            }
        }
    }

    private fun syncTaskCompletions(
        userId: Long,
        dtos: List<TaskCompletionDto>,
    ) {
        if (dtos.isEmpty()) return

        val taskIds = dtos.map { it.taskId }.toSet()
        val createdAtSet = dtos.map { it.createdAt }.toSet()

        val tasksIds =
            TaskDAO
                .find {
                    (TasksTable.localId inList taskIds) and
                        (TasksTable.userId eq userId)
                }.associateBy { it.localId }

        val existing =
            TaskCompletionDAO
                .wrapRows(
                    TaskCompletionsTable
                        .innerJoin(TasksTable)
                        .select(TaskCompletionsTable.columns)
                        .where {
                            (TasksTable.userId eq userId) and
                                (TaskCompletionsTable.createdAt inList createdAtSet)
                        },
                ).map { it.taskId to it.createdAt }
                .toHashSet()

        dtos.forEach { dto ->
            val task = tasksIds[dto.taskId] ?: return@forEach

            val key = task.id to dto.createdAt
            if (key !in existing) {
                TaskCompletionDAO.create(task.id, dto)
            }
        }
    }

    private fun syncUserProfile(
        userId: Long,
        dto: UserProfileDto?,
    ) {
        if (dto == null) return

        require(UserDAO.findById(userId) != null) { "User does not exist." }

        val existing = UserDAO.findById(userId)!!

        if (existing.updatedAt <= dto.updatedAt) return

        existing.updateFrom(dto)
    }

    private fun syncUserStats(
        userId: Long,
        dto: UserStatsDto?,
    ) {
        if (dto == null) return

        require(UserDAO.findById(userId) != null) { "User does not exist." }
        val existing = UserStatsDAO.find { UserStatsTable.userId eq userId }.firstOrNull()!!

        if (existing.updatedAt <= dto.updatedAt) return

        existing.updateFrom(dto)
    }

    private fun syncAppSettings(
        userId: Long,
        dto: AppSettingsDto?,
    ) {
        if (dto == null) return

        require(UserDAO.findById(userId) != null) { "User does not exist." }

        val existing = AppSettingsDAO.find { AppSettingsTable.userId eq userId }.firstOrNull()!!

        if (existing.updatedAt <= dto.updatedAt) return

        existing.updateFrom(dto)
    }

    private fun syncUserAchievements(
        userId: Long,
        achievementIds: Set<Int>,
    ) {
        if (achievementIds.isEmpty()) return

        val existing =
            UserAchievementDAO
                .find { UserAchievementsTable.userId eq userId }
                .map { it.achievementId }
                .toSet()

        val toInsert = achievementIds - existing

        toInsert.forEach { id ->
            UserAchievementDAO.new {
                this.userId = EntityID(userId, UsersTable)
                this.achievementId = id
            }
        }
    }

    private fun syncUserCustomizations(
        userId: Long,
        items: List<CustomizationItemDto>,
    ) {
        if (items.isEmpty()) return

        val existing =
            UserCustomizationDAO
                .find { UserCustomizationsTable.userId eq userId }
                .map { it.key to it.type }
                .toSet()

        items.forEach { item ->
            val key = item.key
            val type = item.type

            if (existing.contains(key to type)) return@forEach

            UserCustomizationDAO.new {
                this.userId = EntityID(userId, UsersTable)
                this.key = key
                this.type = type
            }
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
) = wrapRows(
    HabitRemindersTable
        .innerJoin(HabitsTable)
        .select(HabitRemindersTable.columns)
        .where {
            (HabitRemindersTable.updatedAt greater ts) and
                (HabitsTable.userId eq userId)
        }.withDistinct(),
)

private fun TaskReminderDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = wrapRows(
    TaskRemindersTable
        .innerJoin(TasksTable)
        .select(TaskRemindersTable.columns)
        .where {
            (TaskRemindersTable.updatedAt greater ts) and
                (TasksTable.userId eq userId)
        }.withDistinct(),
)

private fun StatsLogDAO.Companion.findCreatedSince(
    userId: Long,
    ts: Long,
) = find { (StatsLogsTable.userId eq userId) and (StatsLogsTable.createdAt greater ts) }

private fun HabitCompletionDAO.Companion.findCreatedSince(
    userId: Long,
    ts: Long,
) = wrapRows(
    HabitCompletionsTable
        .innerJoin(HabitsTable)
        .select(HabitCompletionsTable.columns)
        .where {
            (HabitCompletionsTable.createdAt greater ts) and
                (HabitsTable.userId eq userId)
        }.withDistinct(),
)

private fun TaskCompletionDAO.Companion.findCreatedSince(
    userId: Long,
    ts: Long,
) = wrapRows(
    TaskCompletionsTable
        .innerJoin(TasksTable)
        .select(TaskCompletionsTable.columns)
        .where {
            (TaskCompletionsTable.createdAt greater ts) and
                (TasksTable.userId eq userId)
        }.withDistinct(),
)

private fun UserDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = find {
    (UsersTable.id eq userId) and (UsersTable.updatedAt greater ts)
}.firstOrNull()

private fun UserStatsDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = find {
    (UserStatsTable.userId eq userId) and
        (UserStatsTable.updatedAt greater ts)
}.firstOrNull()

private fun AppSettingsDAO.Companion.findUpdatedSince(
    userId: Long,
    ts: Long,
) = find {
    (AppSettingsTable.userId eq userId) and
        (AppSettingsTable.updatedAt greater ts)
}.firstOrNull()

private fun UserAchievementDAO.Companion.findUpdatedSince(userId: Long) =
    find {
        (UserAchievementsTable.userId eq userId)
    }.toSet()

private fun UserCustomizationDAO.Companion.findUpdatedSince(userId: Long) =
    find {
        (UserCustomizationsTable.userId eq userId)
    }.toList()
