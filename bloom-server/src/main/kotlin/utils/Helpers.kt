package utils

import com.example.db.daos.*
import com.example.db.tables.*
import db.daos.UserAchievementDAO
import db.daos.UserCustomizationDAO
import db.tables.UserAchievementsTable
import db.tables.UserCustomizationsTable
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun <DTO, DAO : LongEntity> syncEntities(
    incoming: List<DTO>,
    extractId: (DTO) -> Long,
    loadExisting: (List<Long>) -> Map<Long, DAO>,
    shouldUpdate: (DTO, DAO) -> Boolean,
    create: (DTO) -> Unit,
    update: (DTO, DAO) -> Unit,
) {
    if (incoming.isEmpty()) return

    val ids = incoming.map(extractId)
    val existingMap = loadExisting(ids)

    incoming.forEach { dto ->
        val id = extractId(dto)
        val existing = existingMap[id]

        when {
            existing == null -> create(dto)
            shouldUpdate(dto, existing) -> update(dto, existing)
        }
    }
}

fun ApplicationCall.userId(): Long =
    principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("userId")
        ?.asString()
        ?.toLong()
        ?: error("Unauthorized")

suspend fun deleteUser(userId: Long) {
    transaction {
        val habitIds =
            HabitDAO
                .find { HabitsTable.userId eq userId }
                .map { it.id.value }

        val taskIds =
            TaskDAO
                .find { TasksTable.userId eq userId }
                .map { it.id.value }

        if (habitIds.isNotEmpty()) {
            HabitCompletionDAO
                .find {
                    HabitCompletionsTable.habitId inList habitIds
                }.forEach { it.delete() }
            HabitPlantDAO
                .find {
                    HabitPlantsTable.habitId inList habitIds
                }.forEach { it.delete() }
        }

        if (taskIds.isNotEmpty()) {
            TaskCompletionDAO
                .find {
                    TaskCompletionsTable.taskId inList taskIds
                }.forEach { it.delete() }
            SubtaskDAO
                .find { SubtasksTable.taskId inList taskIds }
                .forEach { it.delete() }
        }

        HabitReminderDAO
            .wrapRows(
                HabitRemindersTable
                    .innerJoin(HabitsTable)
                    .select(HabitRemindersTable.columns)
                    .where { HabitsTable.userId eq userId },
            ).forEach { it.delete() }

        TaskReminderDAO
            .wrapRows(
                TaskRemindersTable
                    .innerJoin(TasksTable)
                    .select(TaskRemindersTable.columns)
                    .where { TasksTable.userId eq userId },
            ).forEach { it.delete() }

        HabitDAO
            .find { HabitsTable.userId eq userId }
            .forEach { it.delete() }

        TaskDAO
            .find { TasksTable.userId eq userId }
            .forEach { it.delete() }

        StatsLogDAO
            .find { StatsLogsTable.userId eq userId }
            .forEach { it.delete() }

        AppSettingsDAO
            .find { AppSettingsTable.userId eq userId }
            .forEach { it.delete() }

        UserStatsDAO
            .find { UserStatsTable.userId eq userId }
            .forEach { it.delete() }

        UserAchievementDAO
            .find { UserAchievementsTable.userId eq userId }
            .forEach { it.delete() }

        UserCustomizationDAO
            .find { UserCustomizationsTable.userId eq userId }
            .forEach { it.delete() }

        UserDAO.findById(userId)?.delete()
    }
}
