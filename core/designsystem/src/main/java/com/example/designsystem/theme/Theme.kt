package com.example.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.designsystem.model.AppTheme
import com.example.designsystem.util.ThemeProvider

private val DarkColorScheme =
    darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
    )

val LightRedVineColorScheme =
    lightColorScheme(
        primary = primaryLightRedVine,
        onPrimary = onPrimaryLightRedVine,
        primaryContainer = primaryContainerLightRedVine,
        onPrimaryContainer = onPrimaryContainerLightRedVine,
        secondary = secondaryLightRedVine,
        onSecondary = onSecondaryLightRedVine,
        secondaryContainer = secondaryContainerLightRedVine,
        onSecondaryContainer = onSecondaryContainerLightRedVine,
        tertiary = tertiaryLightRedVine,
        onTertiary = onTertiaryLightRedVine,
        tertiaryContainer = tertiaryContainerLightRedVine,
        onTertiaryContainer = onTertiaryContainerLightRedVine,
        error = errorLightRedVine,
        onError = onErrorLightRedVine,
        errorContainer = errorContainerLightRedVine,
        onErrorContainer = onErrorContainerLightRedVine,
        background = backgroundLightRedVine,
        onBackground = onBackgroundLightRedVine,
        surface = surfaceLightRedVine,
        onSurface = onSurfaceLightRedVine,
        surfaceVariant = surfaceVariantLightRedVine,
        onSurfaceVariant = onSurfaceVariantLightRedVine,
        outline = outlineLightRedVine,
        outlineVariant = outlineVariantLightRedVine,
        scrim = scrimLightRedVine,
        inverseSurface = inverseSurfaceLightRedVine,
        inverseOnSurface = inverseOnSurfaceLightRedVine,
        inversePrimary = inversePrimaryLightRedVine,
        surfaceDim = surfaceDimLightRedVine,
        surfaceBright = surfaceBrightLightRedVine,
        surfaceContainerLowest = surfaceContainerLowestLightRedVine,
        surfaceContainerLow = surfaceContainerLowLightRedVine,
        surfaceContainer = surfaceContainerLightRedVine,
        surfaceContainerHigh = surfaceContainerHighLightRedVine,
        surfaceContainerHighest = surfaceContainerHighestLightRedVine,
    )

val DarkRedVineColorScheme =
    darkColorScheme(
        primary = primaryDarkRedVine,
        onPrimary = onPrimaryDarkRedVine,
        primaryContainer = primaryContainerDarkRedVine,
        onPrimaryContainer = onPrimaryContainerDarkRedVine,
        secondary = secondaryDarkRedVine,
        onSecondary = onSecondaryDarkRedVine,
        secondaryContainer = secondaryContainerDarkRedVine,
        onSecondaryContainer = onSecondaryContainerDarkRedVine,
        tertiary = tertiaryDarkRedVine,
        onTertiary = onTertiaryDarkRedVine,
        tertiaryContainer = tertiaryContainerDarkRedVine,
        onTertiaryContainer = onTertiaryContainerDarkRedVine,
        error = errorDarkRedVine,
        onError = onErrorDarkRedVine,
        errorContainer = errorContainerDarkRedVine,
        onErrorContainer = onErrorContainerDarkRedVine,
        background = backgroundDarkRedVine,
        onBackground = onBackgroundDarkRedVine,
        surface = surfaceDarkRedVine,
        onSurface = onSurfaceDarkRedVine,
        surfaceVariant = surfaceVariantDarkRedVine,
        onSurfaceVariant = onSurfaceVariantDarkRedVine,
        outline = outlineDarkRedVine,
        outlineVariant = outlineVariantDarkRedVine,
        scrim = scrimDarkRedVine,
        inverseSurface = inverseSurfaceDarkRedVine,
        inverseOnSurface = inverseOnSurfaceDarkRedVine,
        inversePrimary = inversePrimaryDarkRedVine,
        surfaceDim = surfaceDimDarkRedVine,
        surfaceBright = surfaceBrightDarkRedVine,
        surfaceContainerLowest = surfaceContainerLowestDarkRedVine,
        surfaceContainerLow = surfaceContainerLowDarkRedVine,
        surfaceContainer = surfaceContainerDarkRedVine,
        surfaceContainerHigh = surfaceContainerHighDarkRedVine,
        surfaceContainerHighest = surfaceContainerHighestDarkRedVine,
    )

@Composable
fun BLOOMTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    val colorScheme: ColorScheme =
        when {
            appTheme == AppTheme.SYSTEM &&
                dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }
            }

            appTheme == AppTheme.SYSTEM -> {
                if (darkTheme) DarkColorScheme else LightColorScheme
            }

            else -> {
                val bundle =
                    requireNotNull(ThemeProvider.get(appTheme)) {
                        "Theme $appTheme is not registered"
                    }

                if (darkTheme) bundle.dark else bundle.light
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
