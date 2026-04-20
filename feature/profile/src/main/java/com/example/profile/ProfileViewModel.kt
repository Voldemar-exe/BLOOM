package com.example.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
//    userRepository: UserRepository
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(ProfileState())
    val profileUiState: StateFlow<ProfileState>
        get() = _profileUiState.asStateFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.LoadProfile -> TODO()
            ProfileAction.OnAchievementsClick -> TODO()
            ProfileAction.OnAvatarClick -> TODO()
            ProfileAction.OnCustomizationClick -> TODO()
            ProfileAction.OnLeaderboardClick -> TODO()
            ProfileAction.OnSettingsClick -> TODO()
            ProfileAction.OnShopClick -> TODO()
            ProfileAction.OnThemeClick -> TODO()
        }
    }
}
