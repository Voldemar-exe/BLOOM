package com.example.profile.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomBackgrounds
import com.example.model.User
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

@KoinViewModel
class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    val state: StateFlow<ProfileState> =
        combine(
            userRepository.user,
            userRepository.stats,
            userRepository.settings,
        ) { user, stats, settings ->
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
        viewModelScope.launch {
            when (action) {
                ProfileAction.OnSettingsClick ->
                    _events.emit(ProfileEvent.NavigateTo(SettingsNavKey))

                ProfileAction.OnParametersClick ->
                    _events.emit(ProfileEvent.NavigateTo(ParametersNavKey))

                ProfileAction.OnAvatarClick ->
                    _events.emit(ProfileEvent.NavigateTo(AvatarChoiceNavKey))

                ProfileAction.OnThemeClick ->
                    _events.emit(ProfileEvent.NavigateTo(ThemeChoiceNavKey))

                ProfileAction.OnAchievementsClick ->
                    _events.emit(ProfileEvent.NavigateTo(AchievementsNavKey))

                ProfileAction.OnStoreClick ->
                    _events.emit(ProfileEvent.NavigateTo(StoreNavKey))

                ProfileAction.OnLeaderboardClick ->
                    _events.emit(ProfileEvent.NavigateTo(LeaderboardNavKey))

                ProfileAction.TestActionSetUser -> {
                    userRepository.updateUser(
                        User(
                            userId = 0L,
                            email = "test@mail.com",
                            username = "Voldemar",
                            avatar = BloomAvatars.VladGuy,
                            background = BloomBackgrounds.BlackSand,
                            color = Color.Green.toArgb(),
                            achievements = emptySet(),
                            purchases = emptyList(),
                        ),
                    )
                }
            }
        }
    }
}
