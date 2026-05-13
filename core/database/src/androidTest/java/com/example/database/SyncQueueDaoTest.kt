package com.example.database

import com.example.database.model.SyncOperation
import com.example.database.model.SyncTypes
import com.example.database.model.entities.SyncQueueEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class SyncQueueDaoTest : DatabaseTest() {
    @Test
    fun insert_and_getPendingList() =
        runBlocking {
            syncQueueDao.insert(
                SyncQueueEntity(
                    entityId = 1,
                    entityType = SyncTypes.TASK,
                    operation = SyncOperation.UPSERT,
                    createdAt = 1L,
                ),
            )

            val result = syncQueueDao.getPendingList()

            Assert.assertEquals(1, result.size)
        }

    @Test
    fun deleteByIds_removesItems() =
        runBlocking {
            syncQueueDao.insert(
                SyncQueueEntity(
                    entityId = 1,
                    entityType = SyncTypes.TASK,
                    operation = SyncOperation.UPSERT,
                    createdAt = 1L,
                ),
            )

            syncQueueDao.deleteByIds(listOf(1))

            Assert.assertTrue(syncQueueDao.getPendingList().isEmpty())
        }

    @Test
    fun insertSync_updatesIfExists() =
        runBlocking {
            val e1 =
                SyncQueueEntity(
                    entityId = 1,
                    entityType = SyncTypes.TASK,
                    operation = SyncOperation.UPSERT,
                    createdAt = 1L,
                )
            val e2 =
                SyncQueueEntity(
                    entityId = 1,
                    entityType = SyncTypes.TASK,
                    operation = SyncOperation.DELETE,
                    createdAt = 2L,
                )

            syncQueueDao.insertSync(e1)
            syncQueueDao.insertSync(e2)

            val list = syncQueueDao.getPendingList()
            Assert.assertEquals(1, list.size)
            Assert.assertEquals(SyncOperation.DELETE, list[0].operation)
        }

    @Test
    fun observeSyncQueue_emitsFlow() =
        runBlocking {
            syncQueueDao.insert(
                SyncQueueEntity(
                    entityId = 1,
                    entityType = SyncTypes.TASK,
                    operation = SyncOperation.UPSERT,
                    createdAt = 1L,
                ),
            )
            val res = syncQueueDao.observeSyncQueue().first()
            Assert.assertEquals(1, res.size)
        }
}