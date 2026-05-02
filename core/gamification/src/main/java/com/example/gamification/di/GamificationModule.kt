package com.example.gamification.di

import com.example.data.repository.GamificationRepository
import com.example.data.repository.UserRepository
import com.example.gamification.GamificationManager
import com.example.gamification.GamificationProcessor
import org.koin.dsl.module

val gamificationModule =
    module {
        single<GamificationProcessor> {
            GamificationManager(
                get<UserRepository>(),
                get<GamificationRepository>(),
            )
        }
    }
