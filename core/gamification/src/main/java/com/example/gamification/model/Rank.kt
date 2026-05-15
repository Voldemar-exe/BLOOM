package com.example.gamification.model

import androidx.compose.material3.ColorScheme

enum class Rank(val ru: String, private val minLevel: Int) {
    NOVICE("Новичок", minLevel = 1),
    GROWER("Садовод", minLevel = 5),
    KEEPER("Хранитель", minLevel = 10),
    MASTER("Мастер", minLevel = 20),
    ELDER("Старейшина", minLevel = 35),
    LEGEND("Легенда", minLevel = 50),
    ;

    fun getTheme(colorScheme: ColorScheme): RankTheme =
        when (this) {
            NOVICE ->
                RankTheme(
                    gradient = listOf(colorScheme.inversePrimary, colorScheme.primary),
                    contentColor = colorScheme.onPrimary,
                )

            GROWER ->
                RankTheme(
                    gradient = listOf(colorScheme.tertiaryContainer, colorScheme.tertiary),
                    contentColor = colorScheme.onTertiary,
                )

            KEEPER ->
                RankTheme(
                    gradient = listOf(colorScheme.secondaryContainer, colorScheme.secondary),
                    contentColor = colorScheme.onSecondary,
                )

            MASTER ->
                RankTheme(
                    gradient = listOf(colorScheme.tertiary, colorScheme.primary),
                    contentColor = colorScheme.onTertiary,
                )

            ELDER ->
                RankTheme(
                    gradient = listOf(colorScheme.errorContainer, colorScheme.error),
                    contentColor = colorScheme.onError,
                )

            LEGEND ->
                RankTheme(
                    gradient = listOf(colorScheme.onErrorContainer, colorScheme.error),
                    contentColor = colorScheme.onPrimary,
                )
        }

    companion object {
        fun fromLevel(level: Int): Rank =
            entries
                .reversed()
                .firstOrNull { level >= it.minLevel }
                ?: NOVICE
    }
}
