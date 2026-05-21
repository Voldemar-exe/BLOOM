package com.example.data.repository

import com.example.data.util.asModel
import com.example.database.dao.GamificationDao
import com.example.database.model.StatsSourceType
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.TaskCompletionEntity
import com.example.database.util.SyncTracker
import com.example.model.HabitCompletion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

interface GamificationRepository {
    val habitsCompletions: Flow<List<HabitCompletion>>

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

    suspend fun isHabitCompletedToday(habitId: Long): Boolean

    suspend fun isTaskCompletedToday(taskId: Long): Boolean

    suspend fun getLastCompletedHabitTime(): Long
}

internal class GamificationRepositoryImpl(
    private val dao: GamificationDao,
    private val tracker: SyncTracker,
) : GamificationRepository {
    override val habitsCompletions: Flow<List<HabitCompletion>>
        get() = dao.observeHabitsCompletions().map { entities -> entities.map { it.asModel() } }

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
        dao.insertHabitCompletionWithSync(completion, tracker)
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
        dao.insertTaskCompletionWithSync(completion, tracker)
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
        dao.insertStatsLogWithSync(log, tracker)
    }

    override suspend fun isHabitCompletedToday(habitId: Long): Boolean {
        val (start, end) = todayRange()
        return dao.hasHabitCompletionToday(habitId, start, end)
    }

    override suspend fun isTaskCompletedToday(taskId: Long): Boolean {
        val (start, end) = todayRange()
        return dao.hasTaskCompletionToday(taskId, start, end)
    }

    override suspend fun getLastCompletedHabitTime(): Long = dao.getLastHabitCompletionTime() ?: 0L

    private fun todayRange(): Pair<Long, Long> {
        val msInDay = 86_400_000L
        val start = (System.currentTimeMillis() / msInDay) * msInDay
        return start to start + msInDay
    }
}
