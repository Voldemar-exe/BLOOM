package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object HabitCompletionsTable : LongIdTable("habit_completions") {
    val userId = reference("user_id", UsersTable.id)
    val habitId = reference("habit_id", HabitsTable.id).index()
    val completedAt = long("completed_at")
    val experienceEarned = integer("experience_earned")
    val coinsEarned = integer("coins_earned")
    val createdAt = long("created_at")
}