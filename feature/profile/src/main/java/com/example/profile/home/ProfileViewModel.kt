package com.example.profile.home

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

    }
}
