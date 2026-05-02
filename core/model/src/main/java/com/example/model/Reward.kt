package com.example.model

data class Reward(
    val experience: Int = 0,
    val coins: Int = 0,
    val levelUp: Boolean = false,
    val newLevel: Int? = null,
    val achievementUnlocked: Int? = null,
)
