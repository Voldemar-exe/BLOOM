package com.example.profile.embedded.achievements

import androidx.compose.runtime.Immutable
import com.example.gamification.model.Achievement
import com.example.model.SortType
import com.example.model.UserStats

@Immutable
data class AchievementState(
    val userStats: UserStats = UserStats.default(),
    val sortType: SortType = SortType.ID_ASC,
    val sortedAchievementsWithProgress: List<Pair<Achievement, Float>> = emptyList(),
)
