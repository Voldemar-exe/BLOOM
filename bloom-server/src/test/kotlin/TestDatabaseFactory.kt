package com.example

import com.example.db.tables.*
import db.tables.UserAchievementsTable
import db.tables.UserCustomizationsTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object TestDatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver",
        )

        transaction {
            SchemaUtils.create(
                UsersTable,
                UserStatsTable,
                AppSettingsTable,
                UserCustomizationsTable,
                UserAchievementsTable,
                HabitsTable,
                HabitPlantsTable,
                HabitRemindersTable,
                TasksTable,
                SubtasksTable,
                TaskRemindersTable,
                StatsLogsTable,
                HabitCompletionsTable,
                TaskCompletionsTable,
            )
        }
    }

    fun clear() {
        transaction {
            SchemaUtils.drop(
                UsersTable,
                UserStatsTable,
                AppSettingsTable,
                UserCustomizationsTable,
                UserAchievementsTable,
                HabitsTable,
                HabitPlantsTable,
                HabitRemindersTable,
                TasksTable,
                SubtasksTable,
                TaskRemindersTable,
                StatsLogsTable,
                HabitCompletionsTable,
                TaskCompletionsTable,
            )
        }
    }
}
