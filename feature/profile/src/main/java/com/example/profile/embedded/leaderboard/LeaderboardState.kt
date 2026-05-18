package com.example.profile.embedded.leaderboard

import androidx.compose.runtime.Immutable
import com.example.model.LeaderboardUser

@Immutable
data class LeaderboardState(
    val users: List<LeaderboardUser> = emptyList(),
    val isLoading: Boolean = false,
    val lastSyncTimestamp: Long = 0L,
)
