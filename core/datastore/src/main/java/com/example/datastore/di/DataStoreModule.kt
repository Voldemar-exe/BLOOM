package com.example.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.datastore.datastore.LeaderboardDataStore
import com.example.datastore.datastore.LeaderboardDataStoreImpl
import com.example.datastore.datastore.UserPreferencesSerializer
import org.koin.core.qualifier.named
import org.koin.dsl.module
import proto.UserPreferences

val dataStoreModule =
    module {

        single<DataStore<UserPreferences>> {
            get<Context>().userDataStore
        }

        single<DataStore<Preferences>>(named("sync_preferences")) {
            get<Context>().syncPreferencesDataStore
        }
        single<DataStore<Preferences>>(named("leaderboard")) {
            get<Context>().leaderboardDataStore
        }

        single<BloomPreferencesDataStore> {
            BloomPreferencesDataStore(get<DataStore<UserPreferences>>())
        }

        single<LeaderboardDataStore> {
            LeaderboardDataStoreImpl(
                get(named("leaderboard")),
            )
        }
    }

val Context.userDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_prefs.pb",
    serializer = UserPreferencesSerializer(),
)

private val Context.syncPreferencesDataStore by preferencesDataStore(name = "sync_preferences")

private val Context.leaderboardDataStore by preferencesDataStore(name = "leaderboard")
