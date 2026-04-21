package com.example.task.di

import com.example.task.TaskViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val taskModule =
    module {
        viewModel<TaskViewModel>()
    }
