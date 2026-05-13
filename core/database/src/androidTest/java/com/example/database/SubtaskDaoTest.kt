package com.example.database

import com.example.database.model.SyncTypes
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class SubtaskDaoTest : DatabaseTest() {
    @Test
    fun insert_and_findSubtask() =
        runBlocking {
            taskDao.upsert(task())
            subtaskDao.upsert(subtask())

            val result = subtaskDao.findById(1)

            Assert.assertNotNull(result)
            Assert.assertEquals("Subtask 1", result?.title)
        }

    @Test
    fun countByTaskId_returnsCorrectCount() =
        runBlocking {
            taskDao.upsert(task())

            subtaskDao.upsert(subtask(id = 1))
            subtaskDao.upsert(subtask(id = 2))
            subtaskDao.upsert(subtask(id = 3))

            Assert.assertEquals(3, subtaskDao.countByTaskId(1))
        }

    @Test
    fun deleteById_removesSubtask() =
        runBlocking {
            taskDao.upsert(task())
            subtaskDao.upsert(subtask())

            subtaskDao.deleteById(1)

            val result = subtaskDao.findById(1)
            Assert.assertNull(result)
        }

    @Test
    fun upsertWithParentSync_tracksBoth() =
        runBlocking {
            taskDao.upsert(task())
            subtaskDao.upsertWithParentSync(subtask(), taskDao, tracker)

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.SUBTASK })
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.TASK })
        }

    @Test
    fun countCheckedByTaskId_returnsZeroIfUnchecked() =
        runBlocking {
            taskDao.upsert(task())
            subtaskDao.upsert(subtask(isChecked = false))

            Assert.assertEquals(0, subtaskDao.countCheckedByTaskId(1))
        }
}