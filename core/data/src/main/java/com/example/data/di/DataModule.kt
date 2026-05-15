package com.example.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
import com.example.data.repository.StatsRepository
import com.example.data.repository.StatsRepositoryImpl
import com.example.data.repository.SyncMetadataRepository
import com.example.data.repository.SyncMetadataRepositoryImpl
import com.example.data.repository.SyncRepository
import com.example.data.repository.SyncRepositoryImpl
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
import com.example.database.dao.StatsDao
import com.example.database.dao.SubtaskDao
import com.example.database.dao.SyncDao
import com.example.database.dao.SyncQueueDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.util.SyncTracker
import com.example.database.util.TransactionRunner
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.api.AuthApi
import com.example.network.api.SyncApi
import org.koin.dsl.module

val dataModule =
    module {
        single<TaskRepository> {
            TaskRepositoryImpl(
                get<TaskDao>(),
                get<SubtaskDao>(),
                get<TaskReminderDao>(),
                get<TaskWithRelationDao>(),
                get<SyncTracker>(),
            )
        }
        single<HabitRepository> {
            HabitRepositoryImpl(
                get<HabitDao>(),
                get<HabitPlantDao>(),
                get<HabitReminderDao>(),
                get<HabitWithRelationDao>(),
                get<SyncTracker>(),
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
            GamificationRepositoryImpl(get<GamificationDao>(), get<SyncTracker>())
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(get<BloomPreferencesDataStore>())
        }

        single<AuthRepository> {
            AuthRepositoryImpl(get<AuthApi>(), get<BloomPreferencesDataStore>())
        }
        single<SyncRepository> {
            SyncRepositoryImpl(
                get<SyncQueueDao>(),
                get<SyncDao>(),
                get<SyncApi>(),
                get<TransactionRunner>(),
            )
        }
        single<DataStore<Preferences>> {
            PreferenceDataStoreFactory.create {
                get<Context>().preferencesDataStoreFile("sync_preferences")
            }
        }
        single<SyncMetadataRepository> { SyncMetadataRepositoryImpl(get()) }
        single<StatsRepository> {
            StatsRepositoryImpl(
                get<StatsDao>(),
                get<BloomPreferencesDataStore>(),
            )
        }
    }
