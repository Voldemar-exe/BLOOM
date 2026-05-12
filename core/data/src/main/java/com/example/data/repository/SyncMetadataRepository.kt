package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

private const val SYNC_PREFS_NAME = "sync_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SYNC_PREFS_NAME)

interface SyncMetadataRepository {
    suspend fun getLastSyncTimestamp(): Long

    suspend fun saveLastSyncTimestamp(timestamp: Long)
}

class SyncMetadataRepositoryImpl(context: Context) : SyncMetadataRepository {
    private val dataStore = context.dataStore
    private val key = longPreferencesKey("last_sync_timestamp")

    override suspend fun getLastSyncTimestamp(): Long {
        try {
            return dataStore.data.first()[key] ?: 0L
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e, "Failed to read lastSyncTimestamp")
            return 0L
        }
    }

    override suspend fun saveLastSyncTimestamp(timestamp: Long) {
        try {
            dataStore.edit { it[key] = timestamp }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e, "Failed to save lastSyncTimestamp")
        }
    }
}
