package com.example.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.datastore.datastore.UserPreferencesSerializer
import org.koin.dsl.module
import proto.UserPreferences

val dataStoreModule =
    module {

        single<DataStore<UserPreferences>> {
            get<Context>().userDataStore
        }

        single<BloomPreferencesDataStore> {
            BloomPreferencesDataStore(get<DataStore<UserPreferences>>())
        }
    }

val Context.userDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_prefs.pb",
    serializer = UserPreferencesSerializer(),
)
