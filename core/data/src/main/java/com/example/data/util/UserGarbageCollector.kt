package com.example.data.util

import com.example.database.util.DatabaseCleaner
import com.example.datastore.datastore.BloomPreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class UserGarbageCollector(
    private val databaseCleaner: DatabaseCleaner,
    private val dataSource: BloomPreferencesDataStore,
) {
    suspend fun clearAll() {
        Timber.d("Starting full data cleanup")

        try {
            withContext(Dispatchers.IO) {
                databaseCleaner.clearAll()
                Timber.d("Room tables cleared")

                dataSource.clearAll()
                Timber.d("DataStore preferences cleared")

                Timber.d("All data cleared successfully")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during data cleanup")
            throw e
        }
    }
}
