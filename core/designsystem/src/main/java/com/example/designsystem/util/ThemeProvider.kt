package com.example.designsystem.util

import com.example.designsystem.model.AppTheme
import com.example.designsystem.model.ForestThemeBundle
import com.example.designsystem.model.OceanThemeBundle
import com.example.designsystem.model.RedVineThemeBundle
import com.example.designsystem.model.SystemThemeBundle
import com.example.designsystem.model.ThemeBundle

object ThemeProvider {
    private val themes: Map<AppTheme, ThemeBundle> =
        mapOf(
            AppTheme.SYSTEM to SystemThemeBundle,
            AppTheme.RED_VINE to RedVineThemeBundle,
            AppTheme.FOREST to ForestThemeBundle,
            AppTheme.OCEAN to OceanThemeBundle,
        )

    fun get(theme: AppTheme): ThemeBundle? = themes[theme]
}
