package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.database.model.SyncOperation

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String,
    val entityId: Long,
    val operation: SyncOperation,
    val payload: String,
    val createdAt: Long,
)
