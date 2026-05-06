package com.example.bloom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NotificationRepository
import com.example.data.repository.SettingsRepository
import com.example.notification.NotificationManager
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationManager: NotificationManager,
) : ViewModel() {
    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            combine(
                settingsRepository.settings,
                notificationRepository.getAllSchedules(),
            ) { settings, reminders ->
                settings to reminders
            }.distinctUntilChanged()
                .collect { (settings, reminders) ->
                    notificationManager.sync(
                        settings = settings,
                        reminders = reminders,
                    )
                }
        }
    }
}
