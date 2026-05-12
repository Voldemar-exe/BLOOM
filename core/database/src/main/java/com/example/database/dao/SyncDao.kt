package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.SyncTimestamp
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskCompletionEntity
import com.example.database.model.entities.TaskEntity
import com.example.database.model.entities.TaskReminderEntity

@Dao
interface SyncDao {
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): HabitEntity?

    @Query("SELECT * FROM habit_plants WHERE habitId = :habitId")
    suspend fun getHabitPlantByHabitId(habitId: Long): HabitPlantEntity?

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Query("SELECT * FROM subtasks WHERE taskId = :taskId")
    suspend fun getSubtaskByTaskId(taskId: Long): List<SubtaskEntity>

    @Query("SELECT * FROM subtasks WHERE id = :id")
    suspend fun getSubtaskById(id: Long): SubtaskEntity?

    @Query("SELECT * FROM habit_reminders WHERE id = :id")
    suspend fun getHabitReminderById(id: Long): HabitReminderEntity?

    @Query("SELECT * FROM task_reminders WHERE id = :id")
    suspend fun getTaskReminderById(id: Long): TaskReminderEntity?

    @Query("SELECT * FROM stats_logs WHERE id = :id")
    suspend fun getStatsLogById(id: Long): StatsLogEntity?

    @Query("SELECT * FROM habit_completions WHERE id = :id")
    suspend fun getHabitCompletionById(id: Long): HabitCompletionEntity?

    @Query("SELECT * FROM task_completions WHERE id = :id")
    suspend fun getTaskCompletionById(id: Long): TaskCompletionEntity?

    @Query("SELECT id, updatedAt FROM habits WHERE id IN (:ids)")
    suspend fun getHabitsTimestamps(ids: List<Long>): List<SyncTimestamp>

    @Query("SELECT id, updatedAt FROM tasks WHERE id IN (:ids)")
    suspend fun getTasksTimestamps(ids: List<Long>): List<SyncTimestamp>

    @Query("SELECT id, updatedAt FROM habit_reminders WHERE id IN (:ids)")
    suspend fun getHabitRemindersTimestamps(ids: List<Long>): List<SyncTimestamp>

    @Query("SELECT id, updatedAt FROM task_reminders WHERE id IN (:ids)")
    suspend fun getTaskRemindersTimestamps(ids: List<Long>): List<SyncTimestamp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabits(entities: List<HabitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabitPlants(entities: List<HabitPlantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTasks(entities: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSubtasks(entities: List<SubtaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabitReminders(entities: List<HabitReminderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTaskReminders(entities: List<TaskReminderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStatsLogs(entities: List<StatsLogEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabitCompletions(entities: List<HabitCompletionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTaskCompletions(entities: List<TaskCompletionEntity>)
}
