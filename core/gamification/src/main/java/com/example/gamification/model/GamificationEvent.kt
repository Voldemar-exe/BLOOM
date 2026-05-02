package com.example.gamification.model

sealed interface GamificationEvent {
    data class HabitCompleted(val habitId: Long, val isStreak: Boolean) : GamificationEvent

    data class TaskCompleted(val taskId: Long) : GamificationEvent

    data class AchievementEarned(val achievementId: Int) : GamificationEvent
}
