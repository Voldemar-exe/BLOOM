package com.example.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object BloomColors {
    const val GREEN = "green"
    const val BLUE = "blue"
    const val PINK = "pink"

    @Composable
    fun resolve(key: String): Color =
        when (key) {
            GREEN -> Color(0xFF4CAF50)
            BLUE -> Color(0xFF2196F3)
            PINK -> Color(0xFFE91E63)
            else -> Color(0xFF4CAF50)
        }

    const val DEFAULT_KEY: String = GREEN
}
