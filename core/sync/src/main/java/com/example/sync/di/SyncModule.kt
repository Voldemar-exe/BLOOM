package com.example.sync.di

import com.example.data.repository.SyncRepository
import com.example.sync.SyncViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val syncModule =
    module {
        viewModel<SyncViewModel> { SyncViewModel(get<SyncRepository>()) }
    }
