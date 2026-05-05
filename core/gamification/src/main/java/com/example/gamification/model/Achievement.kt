package com.example.gamification.model

import androidx.compose.runtime.Immutable

@Immutable
data class Achievement(
    val id: Int,
    val imageKey: String,
    val title: String,
    val description: String,
    val condition: AchievementCondition,
    val rewardXp: Int = 0,
    val rewardCoins: Int = 0,
)
