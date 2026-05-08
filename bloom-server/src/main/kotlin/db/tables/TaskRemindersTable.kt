package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object TaskRemindersTable : LongIdTable("task_reminders") {
    val userId = reference("user_id", UsersTable.id)
    val taskId = reference("task_id", TasksTable.id).index()
    val reminderTime = varchar("reminder_time", 5)
    val isEnabled = bool("is_enabled").default(true)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}
