package com.example.data.di

import com.example.data.repository.TaskRepository
import com.example.data.repository.TaskRepositoryImpl
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import org.koin.dsl.module

val dataModule =
    module {
        single<TaskRepository> {
            TaskRepositoryImpl(
                get<TaskDao>(),
                get<SubtaskDao>(),
                get<TaskReminderDao>(),
                get<TaskWithRelationDao>(),
            )
        }
    }
