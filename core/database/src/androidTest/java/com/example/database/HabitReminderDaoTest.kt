package com.example.database

import com.example.database.model.SyncTypes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class HabitReminderDaoTest : DatabaseTest() {
    @Test
    fun upsert_and_getReminder() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())
            habitReminderDao.upsert(habitReminder())

            val result = habitReminderDao.getReminders(1).first()

            Assert.assertEquals(1, result.size)
        }

    @Test
    fun deleteReminder() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())
            habitReminderDao.upsert(habitReminder())

            habitReminderDao.deleteById(1)

            val result = habitReminderDao.getReminders(1).first()
            Assert.assertTrue(result.isEmpty())
        }

    @Test
    fun upsertWithSync_tracksSync() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())
            habitReminderDao.upsertWithSync(habitReminder(), tracker)

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.HABIT_REMINDER })
        }

    @Test
    fun getAllHabitsWithReminders_returnsFlow() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())
            habitReminderDao.upsert(habitReminder())

            val res = habitReminderDao.getAllHabitsWithReminders().first()
            Assert.assertEquals(1, res.size)
        }
}