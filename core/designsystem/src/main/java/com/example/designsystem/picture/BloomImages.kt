package com.example.designsystem.picture

import com.example.bloom.core.designsystem.R

enum class BloomImages {
    LOGO,
    ;

    companion object {
        val DEFAULT_KEY = LOGO.name

        fun resolve(imageKey: String): Int =
            when (valueOf(imageKey)) {
                LOGO -> R.drawable.logo
            }
    }
}