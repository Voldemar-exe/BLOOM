package com.example.designsystem.picture

import androidx.compose.runtime.Composable
import com.example.bloom.core.designsystem.R

object BloomAvatars {
    const val CONFIDENT_GIRL = "confident_girl"
    const val JUST_GUY = "just_guy"
    const val QUESTION_GUY = "question_guy"
    const val VLAD_GUY = "vlad_guy"

    @Composable
    fun resolve(avatarKey: String): Int = map[avatarKey] ?: map.getValue(DEFAULT_KEY)

    val map: Map<String, Int> =
        mapOf(
            CONFIDENT_GIRL to R.drawable.confident_girl_avatar,
            JUST_GUY to R.drawable.just_guy_avatar,
            QUESTION_GUY to R.drawable.question_guy_avatar,
            VLAD_GUY to R.drawable.vlad_guy_avatar,
        )

    const val DEFAULT_KEY: String = JUST_GUY
}
