package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.SyncOperation
import com.example.database.model.entities.SyncQueueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncQueueDao {
    @Query("SELECT * FROM sync_queue ORDER BY createdAt ASC")
    fun observeSyncQueue(): Flow<List<SyncQueueEntity>>

    @Query("SELECT * FROM sync_queue ORDER BY createdAt ASC")
    suspend fun getPendingList(): List<SyncQueueEntity>

    @Query("DELETE FROM sync_queue WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Insert
    suspend fun insert(entity: SyncQueueEntity)

    @Query(
        """
        UPDATE sync_queue 
        SET operation = :operation, createdAt = :createdAt
        WHERE entityType = :entityType AND entityId = :entityId
    """,
    )
    suspend fun updateByEntity(
        entityType: String,
        entityId: Long,
        operation: SyncOperation,
        createdAt: Long,
    ): Int

    @Transaction
    suspend fun insertSync(entity: SyncQueueEntity) {
        val updated =
            updateByEntity(
                entityType = entity.entityType,
                entityId = entity.entityId,
                operation = entity.operation,
                createdAt = entity.createdAt,
            )

        if (updated == 0) {
            insert(entity)
        }
    }
}
