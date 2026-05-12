package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.SyncTypes
import com.example.database.model.entities.TaskReminderEntity
import com.example.database.model.relationships.TaskWithReminders
import com.example.database.util.SyncTracker
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskReminderDao {
    @Transaction
    @Query("SELECT * FROM task_reminders WHERE taskId = :taskId")
    fun getTaskReminders(taskId: Long): Flow<List<TaskReminderEntity>>

    @Transaction
    @Query("SELECT * FROM tasks")
    fun getAllTasksWithReminders(): Flow<List<TaskWithReminders>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reminder: TaskReminderEntity): Long

    @Update
    suspend fun update(reminder: TaskReminderEntity)

    @Query("DELETE FROM task_reminders WHERE id = :taskReminderId")
    suspend fun deleteById(taskReminderId: Long)

    @Transaction
    suspend fun upsertWithSync(
        reminder: TaskReminderEntity,
        tracker: SyncTracker,
    ) {
        val reminderId = upsert(reminder)
        tracker.trackSync(SyncTypes.TASK_REMINDER, reminderId)
        tracker.trackSync(SyncTypes.TASK, reminder.taskId)
    }
}
