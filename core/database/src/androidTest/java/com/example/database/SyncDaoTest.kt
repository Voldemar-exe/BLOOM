package com.example.database

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class SyncDaoTest : DatabaseTest() {
    @Test
    fun getHabitsTimestamps_returnsMap() =
        runBlocking {
            habitPlantDao.upsertHabit(habit(id = 1))
            habitPlantDao.upsertHabit(habit(id = 2))

            val ts = syncDao.getHabitsTimestamps(listOf(1L, 2L))
            Assert.assertEquals(2, ts.size)
        }

    @Test
    fun upsertHabits_bulkInsert() =
        runBlocking {
            syncDao.upsertHabits(listOf(habit(id = 10), habit(id = 11)))
            Assert.assertNotNull(syncDao.getHabitById(10))
        }

    @Test
    fun getTaskById_returnsNullIfMissing() =
        runBlocking {
            Assert.assertNull(syncDao.getTaskById(999L))
        }
}