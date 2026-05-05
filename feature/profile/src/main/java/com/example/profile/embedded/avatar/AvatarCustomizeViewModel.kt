package com.example.profile.embedded.avatar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.model.CustomizationType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AvatarCustomizeViewModel(private val userRepository: UserRepository) : ViewModel() {
    val state: StateFlow<AvatarCustomizeState> =
        userRepository.user
            .map {
                val user = it!!
                AvatarCustomizeState(
                    avatarKey = user.avatarKey,
                    backgroundKey = user.backgroundKey,
                    colorKey = user.colorKey,
                    username = user.username,
                    email = user.email,
                    ownedItems = user.ownedItems
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AvatarCustomizeState(),
            )

    fun onAction(action: AvatarCustomizeAction) {
        when (action) {
            is AvatarCustomizeAction.OnAvatarSelect ->
                handleNewCustomization(
                    action.avatarKey,
                    CustomizationType.AVATAR,
                )

            is AvatarCustomizeAction.OnBackgroundSelect ->
                handleNewCustomization(
                    action.backgroundKey,
                    CustomizationType.BACKGROUND,
                )

            is AvatarCustomizeAction.OnColorSelect ->
                handleNewCustomization(
                    action.colorKey,
                    CustomizationType.COLOR,
                )
        }
    }

    private fun handleNewCustomization(
        key: String,
        type: CustomizationType,
    ) {
        viewModelScope.launch {
            userRepository.updateCustomization(key, type)
        }
    }
}
