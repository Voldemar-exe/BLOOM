package com.example.designsystem.picture

import androidx.compose.ui.graphics.Color

enum class BloomColors {
    GREEN,
    BLUE,
    PINK,
    RED,
    ORANGE,
    AMBER,
    YELLOW,
    LIME,
    TEAL,
    CYAN,
    INDIGO,
    PURPLE,
    BROWN,
    GRAY,
    ;

    companion object {
        val DEFAULT_KEY = GREEN.name

        fun resolve(colorKey: String): Color =
            when (valueOf(colorKey)) {
                GREEN -> Color(0xFF4CAF50)
                BLUE -> Color(0xFF2196F3)
                PINK -> Color(0xFFE91E63)
                RED -> Color(0xFFE53935)
                ORANGE -> Color(0xFFFF9800)
                AMBER -> Color(0xFFFFB300)
                YELLOW -> Color(0xFFEAB308)
                LIME -> Color(0xFF8BC34A)
                TEAL -> Color(0xFF009688)
                CYAN -> Color(0xFF00BCD4)
                INDIGO -> Color(0xFF3F51B5)
                PURPLE -> Color(0xFF9C27B0)
                BROWN -> Color(0xFF795548)
                GRAY -> Color(0xFF9E9E9E)
            }
    }
}
