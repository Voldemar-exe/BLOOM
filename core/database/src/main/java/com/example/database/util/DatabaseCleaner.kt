package com.example.database.util

import android.content.Context
import com.example.database.BloomDatabase
import org.koin.core.annotation.Singleton
import timber.log.Timber

interface DatabaseCleaner {
    suspend fun clearAll()
}

@Singleton
internal class DatabaseCleanerImpl(
    private val context: Context,
    private val database: BloomDatabase,
) : DatabaseCleaner {
    override suspend fun clearAll() {
        Timber.d("Clearing Room tables")
        database.clearAllTables()

        val dbFile = context.getDatabasePath("bloom-database")
        Timber.d("Deleting DB files: ${dbFile.deleteRecursively()}")
    }
}
