package com.example.data.models

import java.util.UUID

data class User(
    val userId: UUID,
    val email: String,
    val nickname: String,
    val avatarUrl: String,
    val createdAt: Long,
    // TODO Add Data class for User's habit config
    val weeklyGoal: Int,
    val dailyStreakTarget: Int,
    val totalHabitsCompleted: Double,
    val currentStreak: Int,
    val longestStreak: Int,
    val achievements: List<String> // TODO Add Data class for Achievement
)
