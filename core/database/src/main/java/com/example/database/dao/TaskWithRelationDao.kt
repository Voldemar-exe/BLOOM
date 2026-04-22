package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.relationships.TaskAndSubtasks
import com.example.database.model.relationships.TaskWithSubtasksAndReminders
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskWithRelationDao {
    @Transaction
    @Query("SELECT * FROM tasks")
    fun getTasksWithSubtasksAndReminders(): Flow<List<TaskWithSubtasksAndReminders>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskWithSubtasks(taskId: Long): TaskAndSubtasks?

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskWithSubtasksAndReminders(taskId: Long): TaskWithSubtasksAndReminders?
}
