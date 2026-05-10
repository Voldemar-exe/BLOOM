package com.example.data.util

import com.example.data.model.SyncQueue
import com.example.database.model.entities.SyncQueueEntity

fun SyncQueueEntity.toDomain() =
    SyncQueue(
        id = id,
        entityType = entityType,
        entityId = entityId,
        operation = operation,
        payload = payload,
        createdAt = createdAt,
    )

fun SyncQueue.toEntity() =
    SyncQueueEntity(
        id = id,
        entityType = entityType,
        entityId = entityId,
        operation = operation,
        payload = payload,
        createdAt = createdAt,
    )
