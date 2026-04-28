package com.example.profile.home

sealed interface ProfileAction {
    object OnSettingsClick : ProfileAction

    object OnParametersClick : ProfileAction

    object OnAvatarClick : ProfileAction

    object OnThemeClick : ProfileAction

    object OnAchievementsClick : ProfileAction

    object OnStoreClick : ProfileAction

    object OnLeaderboardClick : ProfileAction
}
