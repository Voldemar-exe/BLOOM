package com.example.designsystem.util

import com.example.designsystem.model.AppTheme
import com.example.designsystem.model.RedVineThemeBundle
import com.example.designsystem.model.ThemeBundle

object ThemeProvider {
    private val themes: Map<AppTheme, ThemeBundle> =
        mapOf(
            AppTheme.RED_VINE to RedVineThemeBundle,
            // AppTheme.OCEAN to OceanThemeBundle
            // AppTheme.FOREST to ForestThemeBundle
        )

    fun get(theme: AppTheme): ThemeBundle? = themes[theme]
}
