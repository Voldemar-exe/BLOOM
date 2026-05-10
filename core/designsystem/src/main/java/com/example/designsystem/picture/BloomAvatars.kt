package com.example.designsystem.picture

import com.example.bloom.core.designsystem.R

enum class BloomAvatars {
    CONFIDENT_GIRL,
    CHEERFUL_GIRL,
    CREATIVE_GIRL,
    CALM_GIRL,
    BRIGHT_GIRL,
    GENTLE_GIRL,
    WITTY_GIRL,
    JUST_GUY,
    QUESTION_GUY,
    VLAD_GUY,
    BRAVE_GUY,
    CHILL_GUY,
    FOCUSED_GUY,
    RELIABLE_GUY,
    ;

    companion object {
        val DEFAULT_KEY = JUST_GUY.name

        fun resolve(avatarKey: String): Int =
            when (valueOf(avatarKey)) {
                CONFIDENT_GIRL -> R.drawable.confident_girl_avatar
                CHEERFUL_GIRL -> R.drawable.cheerful_girl_avatar
                CREATIVE_GIRL -> R.drawable.creative_girl_avatar
                CALM_GIRL -> R.drawable.calm_girl_avatar
                BRIGHT_GIRL -> R.drawable.bright_girl_avatar
                GENTLE_GIRL -> R.drawable.gentle_girl_avatar
                WITTY_GIRL -> R.drawable.witty_girl_avatar
                JUST_GUY -> R.drawable.just_guy_avatar
                QUESTION_GUY -> R.drawable.question_guy_avatar
                VLAD_GUY -> R.drawable.vlad_guy_avatar
                BRAVE_GUY -> R.drawable.brave_guy_avatar
                CHILL_GUY -> R.drawable.chill_guy_avatar
                FOCUSED_GUY -> R.drawable.focused_guy_avatar
                RELIABLE_GUY -> R.drawable.reliable_guy_avatar
            }
    }
}
