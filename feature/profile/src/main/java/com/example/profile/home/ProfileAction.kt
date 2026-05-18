package com.example.profile.home

sealed interface ProfileAction {
    object OnSettingsClick : ProfileAction

    object OnAvatarClick : ProfileAction

    object OnThemeClick : ProfileAction

    object OnAchievementsClick : ProfileAction

    object OnStoreClick : ProfileAction

    object OnLeaderboardClick : ProfileAction

    data class OnUserUpdate(
        val username: String,
        val email: String,
        val password: String,
    ) : ProfileAction

    // TODO: Remove later
    object TestActionSetUser : ProfileAction

    object OnExitClick : ProfileAction

    object DeleteAccount : ProfileAction
}
