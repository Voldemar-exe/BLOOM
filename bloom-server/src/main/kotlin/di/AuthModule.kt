package com.example.di

import com.example.auth.AuthService
import com.example.auth.AuthServiceImpl
import org.koin.dsl.module

val authModule =
    module {
        single<AuthService> { AuthServiceImpl() }
    }
