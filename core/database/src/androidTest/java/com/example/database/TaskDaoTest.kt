package com.example.database

import com.example.database.model.SyncTypes
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class TaskDaoTest : DatabaseTest() {
    @Test
    fun upsert_and_getTask() =
        runBlocking {
            taskDao.upsert(task())

            val result = taskDao.getTaskById(1)

            Assert.assertNotNull(result)
            Assert.assertEquals("Task 1", result?.title)
        }

    @Test
    fun updateTaskCompletion_updatesTask() =
        runBlocking {
            taskDao.upsert(task())

            taskDao.updateTaskCompletionAndSync(
                taskId = 1,
                isComplete = true,
                now = 100L,
            )

            val result = taskDao.getTaskById(1)

            Assert.assertTrue(result!!.isChecked)
            Assert.assertEquals(100L, result.updatedAt)
        }

    @Test
    fun upsertWithSync_tracksSync() =
        runBlocking {
            taskDao.upsertWithSync(task(), tracker)

            val queue = syncQueueDao.getPendingList()
            Assert.assertEquals(1, queue.size)
            Assert.assertEquals(SyncTypes.TASK, queue[0].entityType)
        }

    @Test
    fun toggleTaskWithSubtasks_flipsState() =
        runBlocking {
            taskDao.upsert(task())
            taskDao.toggleTaskWithSubtasks(1L, tracker)

            val t = taskDao.getTaskById(1L)
            Assert.assertTrue(t?.isChecked == true)

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.TASK })
        }
}