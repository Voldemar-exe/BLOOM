package com.example.profile.embedded.avatar

sealed interface AvatarCustomizeAction {
    data class OnAvatarSelect(val avatarKey: String) : AvatarCustomizeAction

    data class OnBackgroundSelect(val backgroundKey: String) : AvatarCustomizeAction

    data class OnColorSelect(val colorKey: String) : AvatarCustomizeAction
}
