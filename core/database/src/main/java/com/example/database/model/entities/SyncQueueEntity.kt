package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.database.model.SyncOperation

@Entity(
    tableName = "sync_queue",
    indices = [Index(value = ["entityId", "entityType"], unique = true)]
)
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityId: Long,
    val entityType: String,
    val operation: SyncOperation,
    val createdAt: Long,
)
