package com.example.data.repository

import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.model.AppSettings
import com.example.model.AppearanceSettings
import com.example.model.NotificationSettings
import com.example.model.PreferenceSettings
import com.example.model.ReminderSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsRepository {
    val settings: Flow<AppSettings>
    val appearance: Flow<AppearanceSettings>
    val preferences: Flow<PreferenceSettings>
    val notifications: Flow<NotificationSettings>
    val reminders: Flow<ReminderSettings>

    suspend fun updateSettings(settings: AppSettings)
}

internal class SettingsRepositoryImpl(private val dataStore: BloomPreferencesDataStore) :
    SettingsRepository {
    override val settings: Flow<AppSettings>
        get() = dataStore.settings
    override val appearance: Flow<AppearanceSettings>
        get() = settings.map { AppearanceSettings(it.theme) }
    override val preferences: Flow<PreferenceSettings>
        get() = settings.map { PreferenceSettings(it.weeklyGoal, it.streakTarget) }
    override val notifications: Flow<NotificationSettings>
        get() = settings.map { NotificationSettings(it.emailEnabled, it.pushEnabled) }
    override val reminders: Flow<ReminderSettings>
        get() = settings.map { ReminderSettings(it.habitRemindersEnabled, it.taskRemindersEnabled) }

    override suspend fun updateSettings(settings: AppSettings) {
        dataStore.setSettings(settings = settings)
    }
}
