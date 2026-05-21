package com.example.task.di

import com.example.data.repository.TaskRepository
import com.example.data.repository.UserRepository
import com.example.gamification.GamificationProcessor
import com.example.task.embedded.TaskSetupViewModel
import com.example.task.home.TaskViewModel
import com.example.task.usecases.CompleteTaskUseCase
import com.example.task.usecases.UpdateTaskCreationUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val taskModule =
    module {
        factory<CompleteTaskUseCase> {
            CompleteTaskUseCase(
                get<TaskRepository>(),
                get<GamificationProcessor>(),
            )
        }
        factory<UpdateTaskCreationUseCase> {
            UpdateTaskCreationUseCase(
                get<UserRepository>(),
            )
        }
        viewModel<TaskViewModel> {
            TaskViewModel(
                get<TaskRepository>(),
                get<CompleteTaskUseCase>(),
            )
        }
        viewModel<TaskSetupViewModel> {
            TaskSetupViewModel(
                get<TaskRepository>(),
                get<UpdateTaskCreationUseCase>(),
            )
        }
    }
