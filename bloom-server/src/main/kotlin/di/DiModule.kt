package com.example.di

import com.example.auth.AuthService
import com.example.auth.AuthServiceImpl
import org.koin.dsl.module
import sync.SyncService
import sync.SyncServiceImpl

val diModule =
    module {
        single<AuthService> { AuthServiceImpl() }
        single<SyncService> { SyncServiceImpl() }
    }
