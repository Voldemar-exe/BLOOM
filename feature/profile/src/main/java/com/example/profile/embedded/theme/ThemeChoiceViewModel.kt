package com.example.profile.embedded.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.ThemeRepository
import com.example.designsystem.model.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeChoiceViewModel(private val themeRepository: ThemeRepository) : ViewModel() {
    val theme: StateFlow<String> =
        themeRepository.theme.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppTheme.SYSTEM.name,
        )

    fun onAction(action: ThemeChoiceAction) {
        when (action) {
            is ThemeChoiceAction.OnThemeItemClick -> changeTheme(action.theme)
        }
    }

    private fun changeTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.setTheme(theme.name)
        }
    }
}
