package com.example.profile.embedded.avatar

import androidx.compose.runtime.Immutable
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.picture.BloomColors
import com.example.model.CustomizationItem

@Immutable
data class AvatarCustomizeState(
    val avatarKey: String = BloomAvatars.DEFAULT_KEY,
    val backgroundKey: String = BloomBackgrounds.DEFAULT_KEY,
    val colorKey: String = BloomColors.DEFAULT_KEY,
    val username: String = "",
    val email: String = "",
    val ownedItems: List<CustomizationItem> = emptyList()
)
