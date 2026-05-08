package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object SubtasksTable : LongIdTable("subtasks") {
    val userId = reference("user_id", UsersTable.id)
    val taskId = reference("task_id", TasksTable.id).index()
    val title = text("title")
    val isChecked = bool("is_checked").default(false)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}
