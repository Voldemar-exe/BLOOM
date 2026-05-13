package com.example.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

interface SyncMetadataRepository {
    suspend fun getLastSyncTimestamp(): Long

    suspend fun saveLastSyncTimestamp(timestamp: Long)
}

class SyncMetadataRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    SyncMetadataRepository {
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
