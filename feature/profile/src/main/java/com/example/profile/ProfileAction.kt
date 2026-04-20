package com.example.profile

sealed interface ProfileAction {
    data object OnSettingsClick : ProfileAction

    data object OnCustomizationClick : ProfileAction

    data object OnAvatarClick : ProfileAction

    data object OnThemeClick : ProfileAction

    data object OnAchievementsClick : ProfileAction

    data object OnShopClick : ProfileAction

    data object OnLeaderboardClick : ProfileAction

    data class LoadProfile(
        val profileId: Int,
    ) : ProfileAction
}
