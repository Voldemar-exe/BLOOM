package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.relationships.TaskAndSubtask
import com.example.database.model.relationships.TaskAndTaskReminder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskWithRelationDao {
    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskWithSubtasks(taskId: Long): Flow<TaskAndSubtask?>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskWithReminders(taskId: Long): Flow<TaskAndTaskReminder?>
}