package com.example.designsystem.picture

import androidx.compose.ui.graphics.Color

enum class BloomColors {
    GREEN,
    BLUE,
    PINK,
    ;

    companion object {
        val DEFAULT_KEY = GREEN.name

        fun resolve(colorKey: String): Color =
            when (valueOf(colorKey)) {
                GREEN -> Color(0xFF4CAF50)
                BLUE -> Color(0xFF2196F3)
                PINK -> Color(0xFFE91E63)
            }
    }
}
