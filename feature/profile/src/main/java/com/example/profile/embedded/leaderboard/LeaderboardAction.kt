package com.example.profile.embedded.leaderboard

sealed interface LeaderboardAction {
    data object LoadLeaderboard : LeaderboardAction

    data object RefreshLeaderboard : LeaderboardAction
}
