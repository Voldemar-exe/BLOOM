package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object UserStatsTable : LongIdTable("user_stats") {
    val userId = reference("user_id", UsersTable).uniqueIndex()
    val level = integer("level").default(1)
    val currentExperience = integer("current_experience").default(0)
    val currentCoinsAmount = integer("current_coins_amount").default(0)
    val maxCoinsAmount = integer("max_coins_amount").default(0)
    val totalHabitsCreated = integer("total_habits_created").default(0)
    val totalHabitsCompleted = integer("total_habits_completed").default(0)
    val totalTasksCreated = integer("total_tasks_created").default(0)
    val totalTasksCompleted = integer("total_tasks_completed").default(0)
    val currentStreak = integer("current_streak").default(0)
    val longestStreak = integer("longest_streak").default(0)
    val updatedAt = long("updated_at")
}
