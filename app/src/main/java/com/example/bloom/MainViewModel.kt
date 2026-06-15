package com.example.bloom

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AuthRepository
import com.example.data.repository.NotificationRepository
import com.example.data.repository.SettingsRepository
import com.example.data.repository.SyncRepository
import com.example.notification.NotificationManager
import com.example.sync.SyncScheduler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object Loading : AuthState

    data object Authorized : AuthState

    data class Unauthorized(val isSkipped: Boolean) : AuthState
}

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationManager: NotificationManager,
    private val syncRepository: SyncRepository,
    private val appContext: Context,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val authState: StateFlow<AuthState> =
        authRepository
            .authToken
            .map {
                if (!it?.first.isNullOrBlank()) {
                    AuthState.Authorized
                } else {
                    AuthState.Unauthorized(it?.second ?: false)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AuthState.Loading,
            )

    init {
        observeNotifications()
    }

    @OptIn(FlowPreview::class)
    private fun observeNotifications() {
        viewModelScope.launch {
            combine(
                settingsRepository.settings,
                notificationRepository.getAllSchedules(),
            ) { settings, reminders ->
                settings to reminders
            }.debounce(300)
                .distinctUntilChanged()
                .collect { (settings, reminders) ->
                    notificationManager.sync(
                        settings = settings,
                        reminders = reminders,
                    )
                }
        }
    }

    fun observeSync() {
        viewModelScope.launch {
            syncRepository.observePending().collect {
                SyncScheduler.enqueueImmediateSync(appContext)
            }
        }
    }
}
