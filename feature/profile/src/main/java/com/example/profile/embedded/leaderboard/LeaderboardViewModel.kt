package com.example.profile.embedded.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SocialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderboardViewModel(private val repository: SocialRepository) : ViewModel() {
    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()

    init {
        loadLeaderboard()
    }

    fun onAction(action: LeaderboardAction) {
        when (action) {
            LeaderboardAction.LoadLeaderboard -> loadLeaderboard()
            LeaderboardAction.RefreshLeaderboard -> loadLeaderboard(true)
        }
    }

    private fun loadLeaderboard(forceRefresh: Boolean = false) {
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val users = repository.getLeaderboard(forceRefresh)

            _state.update { current ->
                current.copy(
                    users = users.sortedByDescending { it.score },
                    isLoading = false,
                    lastSyncTimestamp = System.currentTimeMillis(),
                )
            }
        }
    }
}
