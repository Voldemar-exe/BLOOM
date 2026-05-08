package com.example.data.di

import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.GamificationRepository
import com.example.data.repository.GamificationRepositoryImpl
import com.example.data.repository.HabitRepository
import com.example.data.repository.HabitRepositoryImpl
import com.example.data.repository.NotificationRepository
import com.example.data.repository.NotificationRepositoryImpl
import com.example.data.repository.SettingsRepository
import com.example.data.repository.SettingsRepositoryImpl
import com.example.data.repository.TaskRepository
import com.example.data.repository.TaskRepositoryImpl
import com.example.data.repository.ThemeRepository
import com.example.data.repository.ThemeRepositoryImpl
import com.example.data.repository.UserRepository
import com.example.data.repository.UserRepositoryImpl
import com.example.database.dao.GamificationDao
import com.example.database.dao.HabitDao
import com.example.database.dao.HabitPlantDao
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.HabitWithRelationDao
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.api.AuthApi
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

        single<ThemeRepository> {
            ThemeRepositoryImpl(get<BloomPreferencesDataStore>())
        }

        single<NotificationRepository> {
            NotificationRepositoryImpl(
                get<HabitReminderDao>(),
                get<TaskReminderDao>(),
            )
        }

        single<GamificationRepository> {
            GamificationRepositoryImpl(get<GamificationDao>())
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(get<BloomPreferencesDataStore>())
        }

        single<AuthRepository> {
            AuthRepositoryImpl( get<AuthApi>(), get<BloomPreferencesDataStore>())
        }
    }
