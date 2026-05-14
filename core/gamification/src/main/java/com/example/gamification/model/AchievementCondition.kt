package com.example.gamification.model

sealed interface AchievementCondition {
    val target: Int

    fun getProgress(current: Int) = (current.toFloat() / target).coerceIn(0f, 1f)
}

data class HabitCreatedCondition(override val target: Int) : AchievementCondition

data class StreakCondition(override val target: Int) : AchievementCondition

data class HabitCompletedCondition(override val target: Int) : AchievementCondition

data class TaskCreatedCondition(override val target: Int) : AchievementCondition

data class TaskCompletedCondition(override val target: Int) : AchievementCondition

data class LevelCondition(override val target: Int) : AchievementCondition

data class CoinsCondition(override val target: Int) : AchievementCondition
