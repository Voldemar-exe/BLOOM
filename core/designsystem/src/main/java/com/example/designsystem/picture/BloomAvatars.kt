package com.example.designsystem.picture

import com.example.bloom.core.designsystem.R

enum class BloomAvatars {
    CONFIDENT_GIRL,
    JUST_GUY,
    QUESTION_GUY,
    VLAD_GUY,
    ;

    companion object {
        val DEFAULT_KEY = JUST_GUY.name

        fun resolve(avatarKey: String): Int =
            when (valueOf(avatarKey)) {
                CONFIDENT_GIRL -> R.drawable.confident_girl_avatar
                JUST_GUY -> R.drawable.just_guy_avatar
                QUESTION_GUY -> R.drawable.question_guy_avatar
                VLAD_GUY -> R.drawable.vlad_guy_avatar
            }
    }
}
