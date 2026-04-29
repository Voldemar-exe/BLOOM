package com.example.profile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.navigation.Navigator
import com.example.profile.embedded.achievements.AchievementScreen
import com.example.profile.embedded.avatar.AvatarChoiceScreen
import com.example.profile.embedded.leaderboard.LeaderboardScreen
import com.example.profile.embedded.parameters.ParametersScreen
import com.example.profile.embedded.settings.SettingsScreen
import com.example.profile.embedded.store.StoreScreen
import com.example.profile.embedded.theme.ThemeChoiceScreen
import com.example.profile.home.ProfileEvent
import com.example.profile.home.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavKey : NavKey

@Serializable
object SettingsNavKey : NavKey

@Serializable
object ParametersNavKey : NavKey

@Serializable
object AvatarChoiceNavKey : NavKey

@Serializable
object ThemeChoiceNavKey : NavKey

@Serializable
object AchievementsNavKey : NavKey

@Serializable
object StoreNavKey : NavKey

@Serializable
object LeaderboardNavKey : NavKey

fun EntryProviderScope<NavKey>.profileEntry(navigator: Navigator) {
    entry<ProfileNavKey> {
        ProfileScreen(
            onNavigate = { event ->
                when (event) {
                    is ProfileEvent.NavigateTo -> navigator.navigate(event.destination)
                }
            },
        )
    }
    entry<AchievementsNavKey> { AchievementScreen(onBack = { navigator.goBack() }) }
    entry<SettingsNavKey> { SettingsScreen(onBack = { navigator.goBack() }) }
    entry<ParametersNavKey> { ParametersScreen(onBack = { navigator.goBack() }) }
    entry<AvatarChoiceNavKey> { AvatarChoiceScreen(onBack = { navigator.goBack() }) }
    entry<ThemeChoiceNavKey> { ThemeChoiceScreen(onBack = { navigator.goBack() }) }
    entry<StoreNavKey> { StoreScreen(onBack = { navigator.goBack() }) }
    entry<LeaderboardNavKey> { LeaderboardScreen(onBack = { navigator.goBack() }) }
}
