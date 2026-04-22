package com.example.task.di

import com.example.data.repository.TaskRepository
import com.example.task.embedded.TaskSetupViewModel
import com.example.task.home.TaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val taskModule =
    module {
        viewModel<TaskViewModel> {
            TaskViewModel(get<TaskRepository>())
        }
        viewModel<TaskSetupViewModel> {
            TaskSetupViewModel(get<TaskRepository>())
        }
    }
