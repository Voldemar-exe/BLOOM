package com.example.profile.embedded.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.gamification.model.AchievementCondition
import com.example.gamification.model.AchievementRegistry
import com.example.gamification.model.CoinsCondition
import com.example.gamification.model.HabitCompletedCondition
import com.example.gamification.model.HabitCreatedCondition
import com.example.gamification.model.LevelCondition
import com.example.gamification.model.StreakCondition
import com.example.gamification.model.TaskCompletedCondition
import com.example.gamification.model.TaskCreatedCondition
import com.example.model.SortType
import com.example.model.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AchievementViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val sortType = MutableStateFlow(SortType.ID_ASC)

    val state: StateFlow<AchievementState> =
        combine(
            userRepository.stats,
            sortType,
        ) { stats, sortType ->

            val withProgress =
                AchievementRegistry.allAchievements.map { achievement ->
                    achievement to getProgress(achievement.condition, stats)
                }

            val sorted =
                when (sortType) {
                    SortType.ID_ASC ->
                        withProgress.sortedBy { it.first.id }

                    SortType.DESC ->
                        withProgress.sortedByDescending { it.second }

                    SortType.ASC ->
                        withProgress.sortedBy { it.second }
                }

            AchievementState(
                userStats = stats,
                sortType = sortType,
                sortedAchievementsWithProgress = sorted,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AchievementState(),
        )

    fun onAction(action: AchievementAction) {
        when (action) {
            is AchievementAction.OnSortChange -> onSortChanged(action.sortType)
        }
    }

    private fun onSortChanged(newType: SortType) {
        sortType.value = newType
    }
}

internal fun getProgress(
    condition: AchievementCondition,
    userStats: UserStats,
) = when (condition) {
    is HabitCreatedCondition ->
        condition.getProgress(userStats.totalHabitsCreated)

    is StreakCondition ->
        condition.getProgress(userStats.longestStreak)

    is HabitCompletedCondition ->
        condition.getProgress(userStats.totalHabitsCompleted)

    is TaskCreatedCondition ->
        condition.getProgress(userStats.totalTasksCreated)

    is TaskCompletedCondition ->
        condition.getProgress(userStats.totalTasksCompleted)

    is LevelCondition ->
        condition.getProgress(userStats.level)

    is CoinsCondition ->
        condition.getProgress(userStats.currentCoinsAmount)
}
