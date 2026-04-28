package com.example.profile.di

import com.example.data.repository.UserRepository
import com.example.profile.home.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule =
    module {
        viewModel<ProfileViewModel> {
            ProfileViewModel(get<UserRepository>())
        }
    }
