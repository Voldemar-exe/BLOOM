package com.example.database

import com.example.database.model.StatsSourceType
import com.example.database.model.SyncTypes
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.TaskCompletionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class GamificationDaoTest : DatabaseTest() {
    @Test
    fun insertHabitCompletion_and_checkToday() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())

            gamificationDao.insertHabitCompletion(
                HabitCompletionEntity(
                    habitId = 1,
                    completedAt = 1000L,
                    experienceEarned = 10,
                    coinsEarned = 5,
                ),
            )

            val result =
                gamificationDao.hasHabitCompletionToday(
                    habitId = 1,
                    startOfDay = 0L,
                    endOfDay = 2000L,
                )

            Assert.assertTrue(result)
        }

    @Test
    fun insertTaskCompletion_and_checkToday() =
        runBlocking {
            taskDao.upsert(task())

            gamificationDao.insertTaskCompletion(
                TaskCompletionEntity(
                    taskId = 1,
                    completedAt = 1000L,
                    experienceEarned = 10,
                    coinsEarned = 5,
                ),
            )

            val result =
                gamificationDao.hasTaskCompletionToday(
                    taskId = 1,
                    startOfDay = 0L,
                    endOfDay = 2000L,
                )

            Assert.assertTrue(result)
        }

    @Test
    fun insertStatsLog_and_observe() =
        runBlocking {
            gamificationDao.insertStatsLog(
                StatsLogEntity(
                    sourceType = StatsSourceType.HABIT,
                    sourceId = 1,
                    experienceDelta = 10,
                    coinsDelta = 5,
                ),
            )

            val result = gamificationDao.observeUserLogs().first()

            Assert.assertEquals(1, result.size)
        }

    @Test
    fun insertHabitCompletionWithSync_tracks() =
        runBlocking {
            habitPlantDao.upsertHabit(habit())

            gamificationDao.insertHabitCompletionWithSync(
                HabitCompletionEntity(
                    habitId = 1,
                    completedAt = 1000L,
                    experienceEarned = 10,
                    coinsEarned = 5,
                ),
                tracker,
            )

            val queue = syncQueueDao.getPendingList()
            println("$queue")
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.HABIT_COMPLETION })
        }

    @Test
    fun insertTaskCompletionWithSync_tracks() =
        runBlocking {
            taskDao.upsert(task())
            gamificationDao.insertTaskCompletionWithSync(
                TaskCompletionEntity(
                    taskId = 1,
                    completedAt = 1000L,
                    experienceEarned = 10,
                    coinsEarned = 5,
                ),
                tracker,
            )

            val queue = syncQueueDao.getPendingList()
            Assert.assertTrue(queue.any { it.entityType == SyncTypes.TASK_COMPLETION })
        }
}