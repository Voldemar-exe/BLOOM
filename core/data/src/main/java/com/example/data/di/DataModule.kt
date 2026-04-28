package com.example.data.di

import com.example.data.repository.HabitRepository
import com.example.data.repository.HabitRepositoryImpl
import com.example.data.repository.TaskRepository
import com.example.data.repository.TaskRepositoryImpl
import com.example.data.repository.UserRepository
import com.example.data.repository.UserRepositoryImpl
import com.example.database.dao.HabitDao
import com.example.database.dao.HabitPlantDao
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.HabitWithRelationDao
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.datastore.datastore.BloomPreferencesDataStore
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
        single<HabitRepository> {
            HabitRepositoryImpl(
                get<HabitDao>(),
                get<HabitPlantDao>(),
                get<HabitReminderDao>(),
                get<HabitWithRelationDao>(),
            )
        }

        single<UserRepository> {
            UserRepositoryImpl(get<BloomPreferencesDataStore>())
        }
    }
