package com.example.profile.di

import com.example.profile.embedded.achievements.AchievementViewModel
import com.example.profile.embedded.avatar.AvatarCustomizeViewModel
import com.example.profile.embedded.leaderboard.LeaderboardViewModel
import com.example.profile.embedded.settings.SettingsViewModel
import com.example.profile.embedded.store.StoreViewModel
import com.example.profile.embedded.theme.ThemeChoiceViewModel
import com.example.profile.home.ProfileViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val profileModule =
    module {
        viewModel<ProfileViewModel>()
        viewModel<AvatarCustomizeViewModel>()
        viewModel<ThemeChoiceViewModel>()
        viewModel<AchievementViewModel>()
        viewModel<StoreViewModel>()
        viewModel<SettingsViewModel>()
        viewModel<LeaderboardViewModel>()
    }
