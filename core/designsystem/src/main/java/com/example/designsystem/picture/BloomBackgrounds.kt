package com.example.designsystem.picture

import com.example.bloom.core.designsystem.R

enum class BloomBackgrounds {
    BLACK_SAND,
    DARK_PARADISE,
    DUBAI_DOWNTOWN,
    LONELY_TREE,
    MONACO_IPHONE,
    PORSCHE_SMARTPHONE,
    ;

    companion object {
        val DEFAULT_KEY = BLACK_SAND.name

        fun resolve(backgroundKey: String): Int =
            when (valueOf(backgroundKey)) {
                BLACK_SAND -> R.drawable.black_sand_background
                DARK_PARADISE -> R.drawable.dark_paradise_background
                DUBAI_DOWNTOWN -> R.drawable.dubai_downtown_background
                LONELY_TREE -> R.drawable.lonely_tree_background
                MONACO_IPHONE -> R.drawable.monaco_iphone_background
                PORSCHE_SMARTPHONE -> R.drawable.porsche_smartphone_background
            }
    }
}
