package com.example.profile.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _profileUiState = MutableStateFlow(ProfileState())
    val profileUiState: StateFlow<ProfileState>
        get() = _profileUiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _profileUiState.update { it.copy(user = user) }
            }
        }
        viewModelScope.launch {
            userRepository.stats.collect { stats ->
                _profileUiState.update { it.copy(stats = stats) }
            }
        }
    }

    fun onAction(action: ProfileAction) {

    }
}
