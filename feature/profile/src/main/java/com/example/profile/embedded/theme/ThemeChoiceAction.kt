package com.example.profile.embedded.theme

import com.example.designsystem.model.AppTheme

sealed interface ThemeChoiceAction {
    data class OnThemeItemClick(val theme: AppTheme) : ThemeChoiceAction
}
