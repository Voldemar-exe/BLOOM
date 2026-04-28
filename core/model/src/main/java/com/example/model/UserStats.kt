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
    val longestSteak: Int
)
