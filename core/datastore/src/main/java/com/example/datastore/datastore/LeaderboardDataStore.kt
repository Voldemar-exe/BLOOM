package com.example.datastore.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.model.LeaderboardUser
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

interface LeaderboardDataStore {
    suspend fun save(
        users: List<LeaderboardUser>,
        timestamp: Long,
    )

    suspend fun getUsers(): List<LeaderboardUser>

    suspend fun getTimestamp(): Long

    suspend fun isFresh(): Boolean
}

class LeaderboardDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json = Json,
) : LeaderboardDataStore {
    private companion object {
        val USERS_KEY = stringPreferencesKey("leaderboard_users")
        val TIMESTAMP_KEY = longPreferencesKey("leaderboard_timestamp")

        const val CACHE_DURATION = 60 * 60 * 1000L // 1 hour
    }

    override suspend fun save(
        users: List<LeaderboardUser>,
        timestamp: Long,
    ) {
        dataStore.edit { prefs ->
            prefs[USERS_KEY] = json.encodeToString(users)
            prefs[TIMESTAMP_KEY] = timestamp
        }
    }

    override suspend fun getUsers(): List<LeaderboardUser> {
        val prefs = dataStore.data.first()
        val raw = prefs[USERS_KEY] ?: return emptyList()

        return runCatching {
            json.decodeFromString<List<LeaderboardUser>>(raw)
        }.getOrElse {
            emptyList()
        }
    }

    override suspend fun getTimestamp(): Long = dataStore.data.first()[TIMESTAMP_KEY] ?: 0L

    override suspend fun isFresh(): Boolean {
        val timestamp = getTimestamp()
        return System.currentTimeMillis() - timestamp < CACHE_DURATION
    }
}
