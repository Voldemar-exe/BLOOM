package com.example.stats.di

import com.example.data.repository.StatsRepository
import com.example.stats.StatsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val statsModule =
    module {
        viewModel<StatsViewModel> { StatsViewModel(get<StatsRepository>()) }
    }
