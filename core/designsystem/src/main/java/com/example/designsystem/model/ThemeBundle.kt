package com.example.designsystem.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.example.designsystem.theme.DarkColorScheme
import com.example.designsystem.theme.DarkForestColorScheme
import com.example.designsystem.theme.DarkOceanColorScheme
import com.example.designsystem.theme.DarkRedVineColorScheme
import com.example.designsystem.theme.LightColorScheme
import com.example.designsystem.theme.LightForestColorScheme
import com.example.designsystem.theme.LightOceanColorScheme
import com.example.designsystem.theme.LightRedVineColorScheme

data class ThemeBundle(val light: ColorScheme, val dark: ColorScheme)

val SystemThemeBundle =
    ThemeBundle(
        light = LightColorScheme,
        dark = DarkColorScheme,
    )

val RedVineThemeBundle =
    ThemeBundle(
        light = LightRedVineColorScheme,
        dark = DarkRedVineColorScheme,
    )

val ForestThemeBundle =
    ThemeBundle(
        light = LightForestColorScheme,
        dark = DarkForestColorScheme,
    )

val OceanThemeBundle =
    ThemeBundle(
        light = LightOceanColorScheme,
        dark = DarkOceanColorScheme,
    )

fun ThemeBundle.previewColors(isDark: Boolean): List<Color> {
    val scheme = if (isDark) dark else light

    return listOf(
        scheme.primary,
        scheme.secondary,
        scheme.tertiary,
    )
}
