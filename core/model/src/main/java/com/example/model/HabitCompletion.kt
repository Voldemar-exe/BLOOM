package com.example.model

data class HabitCompletion(
    val id: Long,
    val habitId: Long,
    val completedAt: Long,
    val experienceEarned: Int,
    val coinsEarned: Int,
    val createdAt: Long,
)
