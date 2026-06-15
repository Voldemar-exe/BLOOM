package com.example.stats.di

import com.example.stats.StatsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val statsModule =
    module {
        viewModel<StatsViewModel>()
    }
