package com.example.database.util

import com.example.database.dao.SyncQueueDao
import com.example.database.model.SyncOperation
import com.example.database.model.entities.SyncQueueEntity

class SyncTracker(val syncQueueDao: SyncQueueDao) {
    suspend fun trackSync(
        entityType: String,
        entityId: Long,
        syncOperation: SyncOperation = SyncOperation.UPSERT,
    ) {
        syncQueueDao.insertSync(
            SyncQueueEntity(
                entityType = entityType,
                entityId = entityId,
                operation = syncOperation,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}
