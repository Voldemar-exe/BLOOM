package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object AppSettingsTable : LongIdTable("app_settings") {
    val userId = reference("user_id", UsersTable).uniqueIndex()
    val theme = varchar("theme", 50).default("SYSTEM")
    val weeklyGoal = integer("weekly_goal").default(7)
    val streakTarget = integer("streak_target").default(30)
    val emailEnabled = bool("email_enabled").default(true)
    val pushEnabled = bool("push_enabled").default(true)
    val habitRemindersEnabled = bool("habit_reminders_enabled").default(true)
    val taskRemindersEnabled = bool("task_reminders_enabled").default(true)
    val updatedAt = long("updated_at")
}
