package com.example.database

import com.example.database.model.SyncStatus
import com.example.database.model.SyncTypes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class HabitDaoTest : DatabaseTest() {
    @Test
    fun getHabitById_returnsInsertedHabit() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())

            val result = habitDao.getHabitById(1)

            Assert.assertNotNull(result)
            Assert.assertEquals("Habit 1", result?.title)
        }

    @Test
    fun updateHabitCompletion_updatesCheckedState() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())

            habitDao.updateHabitCompletion(
                habitId = 1,
                isChecked = true,
                now = 100L,
            )

            val result = habitDao.getHabitById(1)

            Assert.assertTrue(result!!.isChecked)
            Assert.assertEquals(100L, result.updatedAt)
        }

    @Test
    fun getHabits_returnsOnlyNotDeleted() =
        runBlocking {
            habitPlantDao.upsertHabit(habit(id = 1))
            habitPlantDao.upsertHabit(
                habit(id = 2).copy(syncStatus = SyncStatus.DELETED),
            )

            val result = habitDao.getHabits().first()

            Assert.assertEquals(1, result.size)
            Assert.assertEquals(1L, result.first().id)
        }

    @Test
    fun toggleHabit_tracksSync() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())
            habitDao.toggleHabit(1L, true, tracker)

            val queue = syncQueueDao.getPendingList()
            Assert.assertEquals(1, queue.size)
            Assert.assertEquals(SyncTypes.HABIT, queue[0].entityType)
        }

    @Test
    fun updateSyncStatus_changesFlag() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())
            habitDao.updateSyncStatus(1L, SyncStatus.CHANGED)

            val h = habitDao.getHabitById(1L)
            Assert.assertEquals(SyncStatus.CHANGED, h?.syncStatus)
        }
}