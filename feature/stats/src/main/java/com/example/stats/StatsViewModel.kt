package com.example.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.StatsRepository
import com.example.model.UserStats
import com.example.model.WeeklyActivityData
import com.example.model.WeeklyBySource
import com.example.stats.model.HabitsVsTasksRatio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

class StatsViewModel(private val statsRepository: StatsRepository) : ViewModel() {
    private val _state = MutableStateFlow(StatsState())
    val state: StateFlow<StatsState> = _state.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            combine(
                statsRepository.getUserStats(),
                flow { emit(statsRepository.getWeeklyActivity()) },
            ) { userStats, weeklyData ->
                mapToState(userStats, weeklyData)
            }.collect { state ->
                _state.update { state }
            }
        }
    }

    private fun mapToState(
        userStats: UserStats,
        weekly: WeeklyActivityData,
    ): StatsState =
        StatsState(
            currentExperience = userStats.currentExperience,
            currentCoinsAmount = userStats.currentCoinsAmount,
            maxCoinsAmount = userStats.maxCoinsAmount,
            totalHabitsCreated = userStats.totalHabitsCreated,
            totalHabitsCompleted = userStats.totalHabitsCompleted,
            totalTasksCreated = userStats.totalTasksCreated,
            totalTasksCompleted = userStats.totalTasksCompleted,
            currentStreak = userStats.currentStreak,
            longestStreak = userStats.longestStreak,
            weekDaysLabels = generateWeekLabels(),
            weeklyCompletions = weekly.completions,
            weeklyBySource =
                WeeklyBySource(
                    habitsCompletions = weekly.bySource.habitsCompletions,
                    tasksCompletions = weekly.bySource.tasksCompletions,
                    achievementsUnlocks = weekly.bySource.achievementsUnlocks,
                ),
            habitsVsTasksRatio =
                HabitsVsTasksRatio(
                    completedHabits = userStats.totalHabitsCompleted,
                    completedTasks = userStats.totalTasksCompleted,
                ),
        )

    private fun generateWeekLabels(): List<String> {
        val today = LocalDate.now()
        Timber.d("$today and ${today.dayOfWeek}")
        val startDate = today.plusDays(-6)
        val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

        return (0..6).map { offset ->
            val date = startDate.plusDays(offset.toLong())
            days[date.dayOfWeek.ordinal]
        }
    }
}
