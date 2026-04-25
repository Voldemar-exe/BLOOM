package com.example.habit.di

import com.example.data.repository.HabitRepository
import com.example.habit.home.HabitViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val habitModule =
    module {
        viewModel<HabitViewModel> {
            HabitViewModel(get<HabitRepository>())
        }
    }
