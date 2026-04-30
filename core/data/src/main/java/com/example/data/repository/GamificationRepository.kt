package com.example.data.repository

import com.example.database.dao.GamificationDao
import com.example.database.model.StatsSourceType
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.TaskCompletionEntity
import timber.log.Timber

interface GamificationRepository {
    suspend fun recordHabitCompletion(
        habitId: Long,
        experienceEarned: Int,
        coinsEarned: Int,
    )

    suspend fun recordTaskCompletion(
        taskId: Long,
        experienceEarned: Int,
        coinsEarned: Int,
    )
}

class GamificationRepositoryImpl(private val dao: GamificationDao) : GamificationRepository {
    override suspend fun recordHabitCompletion(
        habitId: Long,
        experienceEarned: Int,
        coinsEarned: Int,
    ) {
        require(experienceEarned >= 0 && coinsEarned >= 0) { "Negative rewards not allowed" }
        val completion =
            HabitCompletionEntity(
                habitId = habitId,
                completedAt = System.currentTimeMillis(),
                experienceEarned = experienceEarned,
                coinsEarned = coinsEarned,
            )
        dao.insertHabitCompletion(completion)
        recordStatsLogInternal(
            sourceType = StatsSourceType.HABIT,
            sourceId = habitId,
            experienceDelta = experienceEarned,
            coinsDelta = coinsEarned,
        )
        Timber.d(
            "Recorded habit completion: $habitId, +$experienceEarned XP, +$coinsEarned coins",
        )
    }

    override suspend fun recordTaskCompletion(
        taskId: Long,
        experienceEarned: Int,
        coinsEarned: Int,
    ) {
        require(experienceEarned >= 0 && coinsEarned >= 0) { "Negative rewards not allowed" }
        val completion =
            TaskCompletionEntity(
                taskId = taskId,
                completedAt = System.currentTimeMillis(),
                experienceEarned = experienceEarned,
                coinsEarned = coinsEarned,
            )
        dao.insertTaskCompletion(completion)
        recordStatsLogInternal(
            sourceType = StatsSourceType.TASK,
            sourceId = taskId,
            experienceDelta = experienceEarned,
            coinsDelta = coinsEarned,
        )
        Timber.d(
            "Recorded task completion: $taskId, +$experienceEarned XP, +$coinsEarned coins",
        )
    }

    private suspend fun recordStatsLogInternal(
        sourceType: StatsSourceType,
        sourceId: Long,
        experienceDelta: Int,
        coinsDelta: Int,
    ) {
        val log =
            StatsLogEntity(
                sourceType = sourceType,
                sourceId = sourceId,
                experienceDelta = experienceDelta,
                coinsDelta = coinsDelta,
                createdAt = System.currentTimeMillis(),
            )
        dao.insertStatsLog(log)
    }
}
