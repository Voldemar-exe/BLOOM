package com.example.habit.di

import com.example.data.repository.GamificationRepository
import com.example.data.repository.HabitRepository
import com.example.gamification.GamificationProcessor
import com.example.habit.embedded.item.HabitSetupViewModel
import com.example.habit.embedded.plant.PlantSetupViewModel
import com.example.habit.home.HabitViewModel
import com.example.habit.usecases.CompleteHabitUseCase
import com.example.habit.usecases.GetHabitsCompletionsUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val habitModule =
    module {
        factory<CompleteHabitUseCase> {
            CompleteHabitUseCase(
                get<HabitRepository>(),
                get<GamificationProcessor>(),
            )
        }
        factory<GetHabitsCompletionsUseCase> {
            GetHabitsCompletionsUseCase(
                get<GamificationRepository>(),
            )
        }
        viewModel<HabitViewModel> {
            HabitViewModel(
                get<HabitRepository>(),
                get<CompleteHabitUseCase>(),
                get<GetHabitsCompletionsUseCase>(),
            )
        }
        viewModel<HabitSetupViewModel> {
            HabitSetupViewModel(get<HabitRepository>())
        }
        viewModel<PlantSetupViewModel> {
            PlantSetupViewModel()
        }
    }
