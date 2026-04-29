package com.example.model

data class UserStats(
    val level: Int,
    val currentExperience: Long,
    val currentCoinsAmount: Int,
    val maxCoinsAmount: Int,
    val totalHabitsCreated: Int,
    val totalHabitsCompleted: Int,
    val totalTasksCreated: Int,
    val totalTasksCompleted: Int,
    val currentStreak: Int,
    val longestSteak: Int,
) {
    companion object {
        fun default() =
            UserStats(
                level = 0,
                currentExperience = 0,
                currentCoinsAmount = 0,
                maxCoinsAmount = 0,
                totalHabitsCreated = 0,
                totalHabitsCompleted = 0,
                totalTasksCreated = 0,
                totalTasksCompleted = 0,
                currentStreak = 0,
                longestSteak = 0,
            )
    }
}
