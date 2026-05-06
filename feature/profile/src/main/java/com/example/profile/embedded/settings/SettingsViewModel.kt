package com.example.profile.embedded.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {
    val state: StateFlow<SettingsState> =
        userRepository.settings
            .map {
                SettingsState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsState(),
            )
    private val settings
        get() = state.value.settings

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.ChangeStreakTarget -> updateStreakTarget(action.target)
            is SettingsAction.ChangeTheme -> updateTheme(action.theme)
            is SettingsAction.ChangeWeeklyGoal -> updateWeeklyGoal(action.goal)
            SettingsAction.ToggleEmail -> toggleEmail()
            SettingsAction.TogglePush -> togglePush()
            SettingsAction.ToggleHabitReminder -> toggleHabitReminders()
            SettingsAction.ToggleTaskReminder -> toggleTaskReminders()
        }
    }

    private fun updateStreakTarget(target: Int) {
        viewModelScope.launch {
            userRepository.updateSettings(settings.copy(streakTarget = target))
        }
    }

    private fun updateTheme(theme: String) {
        viewModelScope.launch {
            userRepository.updateSettings(settings.copy(theme = theme))
        }
    }

    private fun updateWeeklyGoal(goal: Int) {
        viewModelScope.launch {
            userRepository.updateSettings(settings.copy(weeklyGoal = goal))
        }
    }

    private fun toggleEmail() {
        viewModelScope.launch {
            userRepository.updateSettings(settings.copy(emailEnabled = !settings.emailEnabled))
        }
    }

    private fun togglePush() {
        viewModelScope.launch {
            userRepository.updateSettings(settings.copy(pushEnabled = !settings.pushEnabled))
        }
    }

    private fun toggleHabitReminders() {
        viewModelScope.launch {
            userRepository.updateSettings(
                settings.copy(habitRemindersEnabled = !settings.habitRemindersEnabled),
            )
        }
    }

    private fun toggleTaskReminders() {
        viewModelScope.launch {
            userRepository.updateSettings(
                settings.copy(taskRemindersEnabled = !settings.taskRemindersEnabled),
            )
        }
    }
}
