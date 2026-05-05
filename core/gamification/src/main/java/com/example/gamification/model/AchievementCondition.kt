package com.example.gamification.model

sealed interface AchievementCondition {
    val target: Int

    fun getProgress(current: Int) = (current.toFloat() / target).coerceIn(0f, 1f)
}

data class HabitCreatedCondition(override val target: Int) : AchievementCondition

data class StreakCondition(override val target: Int) : AchievementCondition
