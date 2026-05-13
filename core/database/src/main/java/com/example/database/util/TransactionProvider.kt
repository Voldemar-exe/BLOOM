package com.example.database.util

import androidx.room.withTransaction
import com.example.database.BloomDatabase

interface TransactionRunner {
    suspend fun <R> run(block: suspend () -> R): R
}

internal class RoomTransactionRunner(private val db: BloomDatabase) : TransactionRunner {
    override suspend fun <R> run(block: suspend () -> R): R = db.withTransaction(block)
}
