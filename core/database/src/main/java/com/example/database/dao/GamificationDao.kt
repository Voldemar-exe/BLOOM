package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.SyncTypes
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.TaskCompletionEntity
import com.example.database.util.SyncTracker
import kotlinx.coroutines.flow.Flow

@Dao
interface GamificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitCompletion(entity: HabitCompletionEntity): Long

    @Transaction
    suspend fun insertHabitCompletionWithSync(
        entity: HabitCompletionEntity,
        tracker: SyncTracker,
    ) {
        val completedId = insertHabitCompletion(entity)
        tracker.trackSync(SyncTypes.HABIT_COMPLETION, completedId)
    }

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun observeHabitCompletions(habitId: Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions")
    fun observeHabitsCompletions(): Flow<List<HabitCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCompletion(entity: TaskCompletionEntity): Long

    @Transaction
    suspend fun insertTaskCompletionWithSync(
        entity: TaskCompletionEntity,
        tracker: SyncTracker,
    ) {
        val completedId = insertTaskCompletion(entity)
        tracker.trackSync(SyncTypes.TASK_COMPLETION, completedId)
    }

    @Query("SELECT * FROM task_completions WHERE taskId = :taskId ORDER BY completedAt DESC")
    fun observeTaskCompletions(taskId: Long): Flow<List<TaskCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatsLog(entity: StatsLogEntity): Long

    @Transaction
    suspend fun insertStatsLogWithSync(
        entity: StatsLogEntity,
        tracker: SyncTracker,
    ) {
        val completedId = insertStatsLog(entity)
        tracker.trackSync(SyncTypes.STATS_LOG, completedId)
    }

    @Query("SELECT * FROM stats_logs ORDER BY createdAt DESC")
    fun observeUserLogs(): Flow<List<StatsLogEntity>>

    @Query(
        """
            SELECT COUNT(*) > 0 
            FROM habit_completions 
            WHERE habitId = :habitId
            AND completedAt >= :startOfDay 
            AND completedAt < :endOfDay
            """,
    )
    suspend fun hasHabitCompletionToday(
        habitId: Long,
        startOfDay: Long,
        endOfDay: Long,
    ): Boolean

    @Query(
        """
            SELECT COUNT(*) > 0 
            FROM task_completions
            WHERE taskId = :taskId
            AND completedAt >= :startOfDay 
            AND completedAt < :endOfDay
            """,
    )
    suspend fun hasTaskCompletionToday(
        taskId: Long,
        startOfDay: Long,
        endOfDay: Long,
    ): Boolean

    @Query("SELECT completedAt FROM habit_completions ORDER BY completedAt DESC LIMIT 1")
    suspend fun getLastHabitCompletionTime(): Long?
}
