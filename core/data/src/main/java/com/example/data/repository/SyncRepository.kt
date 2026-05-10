package com.example.data.repository

import com.example.data.model.SyncQueue
import com.example.data.util.toDomain
import com.example.data.util.toEntity
import com.example.database.dao.SyncQueueDao
import com.example.network.model.SyncPullResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SyncRepository {
    fun observePending(): Flow<List<SyncQueue>>

    suspend fun deleteByIds(ids: List<Long>)

    suspend fun insert(entity: SyncQueue)

    suspend fun pushChanges(): Result<Unit>

    suspend fun pullChanges(lastSyncTimestamp: Long): Result<SyncPullResponse>
}

class SyncRepositoryImpl(private val dao: SyncQueueDao): SyncRepository {
    override fun observePending(): Flow<List<SyncQueue>> =
        dao.observeSyncQueue().map { entities -> entities.map { it.toDomain() } }

    override suspend fun deleteByIds(ids: List<Long>) {
        dao.deleteByIds(ids)
    }

    override suspend fun insert(entity: SyncQueue) {
        dao.insert(entity.toEntity())
    }

    override suspend fun pushChanges(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun pullChanges(lastSyncTimestamp: Long): Result<SyncPullResponse> {
        TODO("Not yet implemented")
    }
}
