package com.example.designsystem.picture

import androidx.compose.runtime.Composable
import com.example.bloom.core.designsystem.R

object BloomBackgrounds {
    const val BLACK_SAND = "black_sand"

    @Composable
    fun resolve(backgroundKey: String): Int = map[backgroundKey] ?: map.getValue(DEFAULT_KEY)

    val map: Map<String, Int> =
        mapOf(
            BLACK_SAND to R.drawable.black_sand_background,
        )

    const val DEFAULT_KEY: String = BLACK_SAND
}
