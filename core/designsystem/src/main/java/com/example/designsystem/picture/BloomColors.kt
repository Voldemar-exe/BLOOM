package com.example.designsystem.picture

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object BloomColors {
    const val GREEN = "green"
    const val BLUE = "blue"
    const val PINK = "pink"

    @Composable
    fun resolve(colorKey: String): Color =
        map[colorKey] ?: map.getValue(BloomBackgrounds.DEFAULT_KEY)

    val map: Map<String, Color> =
        mapOf(
            GREEN to Color(0xFF4CAF50),
            BLUE to Color(0xFF2196F3),
            PINK to Color(0xFFE91E63),
        )

    const val DEFAULT_KEY: String = GREEN
}
