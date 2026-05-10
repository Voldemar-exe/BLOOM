package com.example.database.util

import com.example.database.dao.SyncQueueDao
import com.example.database.model.SyncOperation
import com.example.database.model.entities.SyncQueueEntity
import kotlinx.serialization.json.Json

class SyncTracker(val syncQueueDao: SyncQueueDao, val json: Json) {
    suspend inline fun <reified T> trackUpsert(
        type: String,
        id: Long,
        entity: T,
    ) {
        syncQueueDao.insert(
            SyncQueueEntity(
                entityType = type,
                entityId = id,
                operation = SyncOperation.UPSERT,
                payload = json.encodeToString(entity),
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun trackDelete(
        type: String,
        id: Long,
    ) {
        syncQueueDao.insert(
            SyncQueueEntity(
                entityType = type,
                entityId = id,
                operation = SyncOperation.DELETE,
                payload = "",
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}