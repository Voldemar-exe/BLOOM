package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object HabitRemindersTable : LongIdTable("habit_reminders") {
    val userId = reference("user_id", UsersTable.id)
    val habitId = reference("habit_id", HabitsTable.id).index()
    val reminderTime = varchar("reminder_time", 5)
    val isEnabled = bool("is_enabled").default(true)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}
