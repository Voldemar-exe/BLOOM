package com.example.profile.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.theme.BloomColors
import com.example.model.User
import com.example.profile.home.ProfileEvent.NavigateTo
import com.example.profile.navigation.AchievementsNavKey
import com.example.profile.navigation.AvatarChoiceNavKey
import com.example.profile.navigation.LeaderboardNavKey
import com.example.profile.navigation.ParametersNavKey
import com.example.profile.navigation.SettingsNavKey
import com.example.profile.navigation.StoreNavKey
import com.example.profile.navigation.ThemeChoiceNavKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    val state: StateFlow<ProfileState> =
        combine(
            userRepository.user,
            userRepository.stats,
            userRepository.settings,
        ) { user, stats, settings ->
            Timber.i("Collected profile with $user, $stats and $settings")
            ProfileState(
                user = user,
                stats = stats,
                settings = settings,
                isLoading = false,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileState(),
        )

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events = _events.asSharedFlow()

    fun onAction(action: ProfileAction) {
        Timber.i("$action")
        viewModelScope.launch {
            when (action) {
                ProfileAction.OnSettingsClick ->
                    _events.emit(NavigateTo(SettingsNavKey))

                ProfileAction.OnParametersClick ->
                    _events.emit(NavigateTo(ParametersNavKey))

                ProfileAction.OnAvatarClick ->
                    _events.emit(NavigateTo(AvatarChoiceNavKey))

                ProfileAction.OnThemeClick ->
                    _events.emit(NavigateTo(ThemeChoiceNavKey))

                ProfileAction.OnAchievementsClick ->
                    _events.emit(NavigateTo(AchievementsNavKey))

                ProfileAction.OnStoreClick ->
                    _events.emit(NavigateTo(StoreNavKey))

                ProfileAction.OnLeaderboardClick ->
                    _events.emit(NavigateTo(LeaderboardNavKey))

                ProfileAction.TestActionSetUser -> {
                    userRepository.updateUser(
                        User(
                            userId = 0L,
                            email = "test@mail.com",
                            username = "Voldemar",
                            avatarKey = BloomAvatars.DEFAULT_KEY,
                            backgroundKey = BloomBackgrounds.DEFAULT_KEY,
                            colorKey = BloomColors.DEFAULT_KEY,
                            ownedAchievements = emptySet(),
                            ownedItems = emptyList(),
                        ),
                    )
                }

                is ProfileAction.OnUserUpdate -> {
                    if (action.username != state.value.user!!.username) {
                        userRepository.updateUsername(action.username)
                    }
                    if (action.email != state.value.user!!.email) {
                        userRepository.updateEmail(action.email)
                    }
                    if (action.password.isNotEmpty()) {
                        userRepository.updatePassword(action.password)
                    }
                }
            }
        }
    }
}
