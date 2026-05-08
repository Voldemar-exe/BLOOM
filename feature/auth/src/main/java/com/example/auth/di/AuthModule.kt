package com.example.auth.di

import com.example.auth.AuthViewModel
import com.example.data.repository.AuthRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule =
    module {
        viewModel<AuthViewModel> {
            AuthViewModel(get<AuthRepository>())
        }
    }
