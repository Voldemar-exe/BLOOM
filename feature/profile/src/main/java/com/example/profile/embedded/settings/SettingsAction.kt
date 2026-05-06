package com.example.profile.embedded.settings

sealed interface SettingsAction {
    data object ToggleEmail : SettingsAction

    data object TogglePush : SettingsAction

    data object ToggleHabitReminder : SettingsAction

    data object ToggleTaskReminder : SettingsAction

    data class ChangeTheme(val theme: String) : SettingsAction

    data class ChangeWeeklyGoal(val goal: Int) : SettingsAction

    data class ChangeStreakTarget(val target: Int) : SettingsAction
}
