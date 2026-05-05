package com.example.designsystem.picture

import com.example.bloom.core.designsystem.R

enum class BloomBackgrounds {
    BLACK_SAND,
    ;

    companion object {
        val DEFAULT_KEY = BLACK_SAND.name

        fun resolve(backgroundKey: String): Int =
            when (valueOf(backgroundKey)) {
                BLACK_SAND -> R.drawable.black_sand_background
            }
    }
}
