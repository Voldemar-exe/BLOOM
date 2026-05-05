package com.example.profile.di

import com.example.data.repository.ThemeRepository
import com.example.data.repository.UserRepository
import com.example.profile.embedded.achievements.AchievementViewModel
import com.example.profile.embedded.avatar.AvatarCustomizeViewModel
import com.example.profile.embedded.theme.ThemeChoiceViewModel
import com.example.profile.home.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule =
    module {
        viewModel<ProfileViewModel> {
            ProfileViewModel(get<UserRepository>())
        }
        viewModel<AvatarCustomizeViewModel> {
            AvatarCustomizeViewModel(get<UserRepository>())
        }
        viewModel<ThemeChoiceViewModel> {
            ThemeChoiceViewModel(get<ThemeRepository>())
        }
        viewModel<AchievementViewModel> {
            AchievementViewModel(get<UserRepository>())
        }
    }
