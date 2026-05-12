package com.example.data.model

import com.example.database.model.SyncOperation

data class SyncQueue(
    val id: Long = 0,
    val entityType: String,
    val entityId: Long,
    val operation: SyncOperation,
    val createdAt: Long,
)
