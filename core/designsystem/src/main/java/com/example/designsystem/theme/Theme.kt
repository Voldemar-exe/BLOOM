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

val DarkColorScheme =
    darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80,
    )

val LightColorScheme =
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

val LightForestColorScheme =
    lightColorScheme(
        primary = primaryLightForest,
        onPrimary = onPrimaryLightForest,
        primaryContainer = primaryContainerLightForest,
        onPrimaryContainer = onPrimaryContainerLightForest,
        secondary = secondaryLightForest,
        onSecondary = onSecondaryLightForest,
        secondaryContainer = secondaryContainerLightForest,
        onSecondaryContainer = onSecondaryContainerLightForest,
        tertiary = tertiaryLightForest,
        onTertiary = onTertiaryLightForest,
        tertiaryContainer = tertiaryContainerLightForest,
        onTertiaryContainer = onTertiaryContainerLightForest,
        error = errorLightForest,
        onError = onErrorLightForest,
        errorContainer = errorContainerLightForest,
        onErrorContainer = onErrorContainerLightForest,
        background = backgroundLightForest,
        onBackground = onBackgroundLightForest,
        surface = surfaceLightForest,
        onSurface = onSurfaceLightForest,
        surfaceVariant = surfaceVariantLightForest,
        onSurfaceVariant = onSurfaceVariantLightForest,
        outline = outlineLightForest,
        outlineVariant = outlineVariantLightForest,
        scrim = scrimLightForest,
        inverseSurface = inverseSurfaceLightForest,
        inverseOnSurface = inverseOnSurfaceLightForest,
        inversePrimary = inversePrimaryLightForest,
        surfaceDim = surfaceDimLightForest,
        surfaceBright = surfaceBrightLightForest,
        surfaceContainerLowest = surfaceContainerLowestLightForest,
        surfaceContainerLow = surfaceContainerLowLightForest,
        surfaceContainer = surfaceContainerLightForest,
        surfaceContainerHigh = surfaceContainerHighLightForest,
        surfaceContainerHighest = surfaceContainerHighestLightForest,
    )

val DarkForestColorScheme =
    darkColorScheme(
        primary = primaryDarkForest,
        onPrimary = onPrimaryDarkForest,
        primaryContainer = primaryContainerDarkForest,
        onPrimaryContainer = onPrimaryContainerDarkForest,
        secondary = secondaryDarkForest,
        onSecondary = onSecondaryDarkForest,
        secondaryContainer = secondaryContainerDarkForest,
        onSecondaryContainer = onSecondaryContainerDarkForest,
        tertiary = tertiaryDarkForest,
        onTertiary = onTertiaryDarkForest,
        tertiaryContainer = tertiaryContainerDarkForest,
        onTertiaryContainer = onTertiaryContainerDarkForest,
        error = errorDarkForest,
        onError = onErrorDarkForest,
        errorContainer = errorContainerDarkForest,
        onErrorContainer = onErrorContainerDarkForest,
        background = backgroundDarkForest,
        onBackground = onBackgroundDarkForest,
        surface = surfaceDarkForest,
        onSurface = onSurfaceDarkForest,
        surfaceVariant = surfaceVariantDarkForest,
        onSurfaceVariant = onSurfaceVariantDarkForest,
        outline = outlineDarkForest,
        outlineVariant = outlineVariantDarkForest,
        scrim = scrimDarkForest,
        inverseSurface = inverseSurfaceDarkForest,
        inverseOnSurface = inverseOnSurfaceDarkForest,
        inversePrimary = inversePrimaryDarkForest,
        surfaceDim = surfaceDimDarkForest,
        surfaceBright = surfaceBrightDarkForest,
        surfaceContainerLowest = surfaceContainerLowestDarkForest,
        surfaceContainerLow = surfaceContainerLowDarkForest,
        surfaceContainer = surfaceContainerDarkForest,
        surfaceContainerHigh = surfaceContainerHighDarkForest,
        surfaceContainerHighest = surfaceContainerHighestDarkForest,
    )

val LightOceanColorScheme =
    lightColorScheme(
        primary = primaryLightOcean,
        onPrimary = onPrimaryLightOcean,
        primaryContainer = primaryContainerLightOcean,
        onPrimaryContainer = onPrimaryContainerLightOcean,
        secondary = secondaryLightOcean,
        onSecondary = onSecondaryLightOcean,
        secondaryContainer = secondaryContainerLightOcean,
        onSecondaryContainer = onSecondaryContainerLightOcean,
        tertiary = tertiaryLightOcean,
        onTertiary = onTertiaryLightOcean,
        tertiaryContainer = tertiaryContainerLightOcean,
        onTertiaryContainer = onTertiaryContainerLightOcean,
        error = errorLightOcean,
        onError = onErrorLightOcean,
        errorContainer = errorContainerLightOcean,
        onErrorContainer = onErrorContainerLightOcean,
        background = backgroundLightOcean,
        onBackground = onBackgroundLightOcean,
        surface = surfaceLightOcean,
        onSurface = onSurfaceLightOcean,
        surfaceVariant = surfaceVariantLightOcean,
        onSurfaceVariant = onSurfaceVariantLightOcean,
        outline = outlineLightOcean,
        outlineVariant = outlineVariantLightOcean,
        scrim = scrimLightOcean,
        inverseSurface = inverseSurfaceLightOcean,
        inverseOnSurface = inverseOnSurfaceLightOcean,
        inversePrimary = inversePrimaryLightOcean,
        surfaceDim = surfaceDimLightOcean,
        surfaceBright = surfaceBrightLightOcean,
        surfaceContainerLowest = surfaceContainerLowestLightOcean,
        surfaceContainerLow = surfaceContainerLowLightOcean,
        surfaceContainer = surfaceContainerLightOcean,
        surfaceContainerHigh = surfaceContainerHighLightOcean,
        surfaceContainerHighest = surfaceContainerHighestLightOcean,
    )

val DarkOceanColorScheme =
    darkColorScheme(
        primary = primaryDarkOcean,
        onPrimary = onPrimaryDarkOcean,
        primaryContainer = primaryContainerDarkOcean,
        onPrimaryContainer = onPrimaryContainerDarkOcean,
        secondary = secondaryDarkOcean,
        onSecondary = onSecondaryDarkOcean,
        secondaryContainer = secondaryContainerDarkOcean,
        onSecondaryContainer = onSecondaryContainerDarkOcean,
        tertiary = tertiaryDarkOcean,
        onTertiary = onTertiaryDarkOcean,
        tertiaryContainer = tertiaryContainerDarkOcean,
        onTertiaryContainer = onTertiaryContainerDarkOcean,
        error = errorDarkOcean,
        onError = onErrorDarkOcean,
        errorContainer = errorContainerDarkOcean,
        onErrorContainer = onErrorContainerDarkOcean,
        background = backgroundDarkOcean,
        onBackground = onBackgroundDarkOcean,
        surface = surfaceDarkOcean,
        onSurface = onSurfaceDarkOcean,
        surfaceVariant = surfaceVariantDarkOcean,
        onSurfaceVariant = onSurfaceVariantDarkOcean,
        outline = outlineDarkOcean,
        outlineVariant = outlineVariantDarkOcean,
        scrim = scrimDarkOcean,
        inverseSurface = inverseSurfaceDarkOcean,
        inverseOnSurface = inverseOnSurfaceDarkOcean,
        inversePrimary = inversePrimaryDarkOcean,
        surfaceDim = surfaceDimDarkOcean,
        surfaceBright = surfaceBrightDarkOcean,
        surfaceContainerLowest = surfaceContainerLowestDarkOcean,
        surfaceContainerLow = surfaceContainerLowDarkOcean,
        surfaceContainer = surfaceContainerDarkOcean,
        surfaceContainerHigh = surfaceContainerHighDarkOcean,
        surfaceContainerHighest = surfaceContainerHighestDarkOcean,
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
