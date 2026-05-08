package com.example.bloom.di

import com.example.bloom.MainViewModel
import com.example.data.repository.AuthRepository
import com.example.data.repository.NotificationRepository
import com.example.data.repository.SettingsRepository
import com.example.notification.NotificationManager
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        viewModel<MainViewModel> {
            MainViewModel(
                get<SettingsRepository>(),
                get<NotificationRepository>(),
                get<NotificationManager>(),
                get<AuthRepository>(),
            )
        }
    }
