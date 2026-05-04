package com.example.profile.embedded.avatar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.picture.BloomColors
import com.example.model.CustomizationItem
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
                    // TODO: Made it default
                    ownedItems =
                        listOf(
                            CustomizationItem(
                                BloomAvatars.DEFAULT_KEY,
                                CustomizationType.AVATAR,
                            ),
                            CustomizationItem(
                                BloomBackgrounds.DEFAULT_KEY,
                                CustomizationType.BACKGROUND,
                            ),
                            CustomizationItem(
                                BloomColors.DEFAULT_KEY,
                                CustomizationType.COLOR,
                            ),
                        ),
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
