package com.example.designsystem.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.example.designsystem.theme.DarkRedVineColorScheme
import com.example.designsystem.theme.LightRedVineColorScheme

data class ThemeBundle(val light: ColorScheme, val dark: ColorScheme)

val RedVineThemeBundle =
    ThemeBundle(
        light = LightRedVineColorScheme,
        dark = DarkRedVineColorScheme,
    )

fun ThemeBundle.previewColors(isDark: Boolean): List<Color> {
    val scheme = if (isDark) dark else light

    return listOf(
        scheme.primary,
        scheme.secondary,
        scheme.tertiary,
    )
}
