package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object TaskCompletionsTable : LongIdTable("task_completions") {
    val taskId = reference("task_id", TasksTable.id).index()
    val completedAt = long("completed_at")
    val experienceEarned = integer("experience_earned")
    val coinsEarned = integer("coins_earned")
    val createdAt = long("created_at").index()
}
