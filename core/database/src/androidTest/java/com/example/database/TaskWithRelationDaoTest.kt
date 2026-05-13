package com.example.database

import com.example.database.model.SyncOperation
import com.example.database.model.SyncTypes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class TaskWithRelationDaoTest : DatabaseTest() {
    @Test
    fun searchTasksWithRelations_filtersByQuery() =
        runBlocking {
            taskDao.upsert(task(title = "Code Review"))
            val res = taskWithRelationDao.searchTasksWithRelations("Code").first()
            Assert.assertEquals(1, res.size)
        }

    @Test
    fun softDeleteTaskCascade_marksDeleted() =
        runBlocking {
            taskDao.upsert(task())
            taskWithRelationDao.softDeleteTaskCascade(1L, tracker)

            val fetched = taskDao.getTaskById(1L)
            Assert.assertNull(fetched)

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(
                queue.any {
                    it.entityType == SyncTypes.TASK &&
                        it.operation == SyncOperation.DELETE
                },
            )
        }
}
