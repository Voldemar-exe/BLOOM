package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.TaskReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskReminderDao {
    @Query("SELECT * FROM task_reminders WHERE taskId = :taskId")
    fun getTaskReminders(taskId: Long): Flow<List<TaskReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reminder: TaskReminderEntity)

    @Update
    suspend fun update(reminder: TaskReminderEntity)

    @Transaction
    suspend fun upsertWithParentSync(
        reminder: TaskReminderEntity,
        taskDao: TaskDao,
    ) {
        upsert(reminder)
        taskDao.updateSyncStatus(reminder.taskId)
    }

    @Transaction
    suspend fun updateWithParentSync(
        reminder: TaskReminderEntity,
        taskDao: TaskDao,
    ) {
        update(reminder)
        taskDao.updateSyncStatus(reminder.taskId)
    }
}
