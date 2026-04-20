package com.example.profile

import androidx.compose.ui.graphics.Color
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomImages

data class ProfileState(
    val username: String = "Nick",
    val rankTitle: String = "Novice",
    val email: String = "test@email.com",
    val level: Int = 0,
    val experience: Long = 0,
    val progress: Float = 0.0f,
    val coins: Int = 0,
    val achievements: List<Int> = emptyList(),
    val avatar: Int = BloomAvatars.VladGuy,
    val background: Int = BloomImages.BlackSand,
    val color: Color = Color.Red,
    val isLoading: Boolean = false,
    val error: String? = null,
)
