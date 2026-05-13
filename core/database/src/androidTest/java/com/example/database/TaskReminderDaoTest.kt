package com.example.database

import com.example.database.model.SyncTypes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class TaskReminderDaoTest : DatabaseTest() {
    @Test
    fun upsert_and_getReminder() =
        runBlocking {
            taskDao.upsert(task())
            taskReminderDao.upsert(taskReminder())

            val result = taskReminderDao.getTaskReminders(1).first()

            Assert.assertEquals(1, result.size)
        }

    @Test
    fun deleteReminder() =
        runBlocking {
            taskDao.upsert(task())
            taskReminderDao.upsert(taskReminder())

            taskReminderDao.deleteById(1)

            val result = taskReminderDao.getTaskReminders(1).first()
            Assert.assertTrue(result.isEmpty())
        }

    @Test
    fun upsertWithSync_tracksBoth() =
        runBlocking {
            taskDao.upsert(task())
            taskReminderDao.upsertWithSync(taskReminder(), tracker)

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.TASK_REMINDER })
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.TASK })
        }
}