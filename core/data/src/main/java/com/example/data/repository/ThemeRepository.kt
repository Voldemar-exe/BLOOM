package com.example.data.repository

import com.example.datastore.datastore.BloomPreferencesDataStore
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val theme: Flow<String>

    suspend fun setTheme(theme: String)
}

class ThemeRepositoryImpl(private val dataSource: BloomPreferencesDataStore) : ThemeRepository {
    override val theme: Flow<String> = dataSource.theme

    override suspend fun setTheme(theme: String) {
        dataSource.setTheme(theme)
    }
}
