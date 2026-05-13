package com.example.database

import com.example.database.model.SyncOperation
import com.example.database.model.SyncTypes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class HabitWithRelationDaoTest : DatabaseTest() {
    @Test
    fun searchHabitsWithRelations_filtersByQuery() =
        runBlocking {
            habitPlantDao.upsertHabitWithPlant(
                habit(title = "Morning Run"),
                plant(),
                tracker,
            )
            val res = habitWithRelationDao.searchHabitsWithRelations("Run").first()
            Assert.assertEquals(1, res.size)
        }

    @Test
    fun softDeleteHabitCascade_marksDeleted() =
        runBlocking {
            val h = habit()
            habitPlantDao.upsertHabit(h)
            habitWithRelationDao.softDeleteHabitCascade(h, tracker)

            val fetched = habitDao.getHabitById(1L)
            Assert.assertNull(fetched)

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(
                queue.any {
                    it.entityType == SyncTypes.HABIT && it.operation == SyncOperation.DELETE
                },
            )
        }

    @Test
    fun getHabitWithPlantAndReminders_returnsData() =
        runBlocking {
            val h = habit()
            habitPlantDao.upsertHabitWithPlant(h, plant(), tracker)
            habitReminderDao.upsert(habitReminder())

            val res = habitWithRelationDao.getHabitWithPlantAndReminders(1L)
            Assert.assertNotNull(res)
        }
}