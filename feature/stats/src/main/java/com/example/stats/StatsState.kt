package com.example.stats

import com.example.model.WeeklyBySource
import com.example.stats.model.HabitsVsTasksRatio

data class StatsState(
    val currentExperience: Int = 0,
    val experienceToNextLevel: Int = 100,
    val currentCoinsAmount: Int = 0,
    val maxCoinsAmount: Int = 1000,
    val totalHabitsCreated: Int = 0,
    val totalHabitsCompleted: Int = 0,
    val totalTasksCreated: Int = 0,
    val totalTasksCompleted: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val weekDaysLabels: List<String> = listOf(),
    val weeklyCompletions: List<Int> = listOf(0, 0, 0, 0, 0, 0, 0),
    val weeklyBySource: WeeklyBySource =
        WeeklyBySource(
            habitsCompletions = listOf(0, 0, 0, 0, 0, 0, 0),
            tasksCompletions = listOf(0, 0, 0, 0, 0, 0, 0),
            achievementsUnlocks = listOf(0, 0, 0, 0, 0, 0, 0),
        ),
    val habitsVsTasksRatio: HabitsVsTasksRatio = HabitsVsTasksRatio(),
)
