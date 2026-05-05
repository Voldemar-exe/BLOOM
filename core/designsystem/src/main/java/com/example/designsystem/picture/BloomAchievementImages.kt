package com.example.designsystem.picture

import com.example.bloom.core.designsystem.R

enum class BloomAchievementImages {
    PLACEHOLDER,
    ;

    companion object {
        val DEFAULT_KEY = PLACEHOLDER

        fun resolve(imageKey: String): Int =
            when (valueOf(imageKey)) {
                PLACEHOLDER -> R.drawable.placeholder
            }
    }
}
