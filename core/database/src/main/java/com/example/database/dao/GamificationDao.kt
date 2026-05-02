package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.TaskCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitCompletion(entity: HabitCompletionEntity): Long

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun observeHabitCompletions(habitId: Long): Flow<List<HabitCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCompletion(entity: TaskCompletionEntity): Long

    @Query("SELECT * FROM task_completions WHERE taskId = :taskId ORDER BY completedAt DESC")
    fun observeTaskCompletions(taskId: Long): Flow<List<TaskCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatsLog(entity: StatsLogEntity): Long

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
}
