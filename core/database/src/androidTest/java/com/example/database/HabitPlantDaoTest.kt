package com.example.database

import com.example.database.model.SyncTypes
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class HabitPlantDaoTest : DatabaseTest() {
    @Test
    fun insert_and_getPlant() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())
            habitPlantDao.upsertPlant(plant())

            val result = habitPlantDao.getByHabitId(1)

            Assert.assertNotNull(result)
            Assert.assertEquals(1L, result?.habitId)
        }

    @Test
    fun upsertHabitWithPlant_tracksSync() =
        runBlocking {
            val id = habitPlantDao.upsertHabitWithPlant(habit(), plant(), tracker)
            Assert.assertNotNull(id)

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.HABIT && it.entityId == id })
        }
}