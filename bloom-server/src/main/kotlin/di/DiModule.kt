package com.example.di

import com.example.auth.AuthService
import com.example.auth.AuthServiceImpl
import com.example.sync.SocialService
import com.example.sync.SocialServiceImpl
import org.koin.dsl.module
import sync.SyncService
import sync.SyncServiceImpl

val diModule =
    module {
        single<AuthService> { AuthServiceImpl() }
        single<SyncService> { SyncServiceImpl() }
        single<SocialService> { SocialServiceImpl() }
    }
