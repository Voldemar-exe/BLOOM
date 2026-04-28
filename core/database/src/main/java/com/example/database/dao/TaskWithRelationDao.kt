package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.relationships.TaskWithSubtasks
import com.example.database.model.relationships.TaskWithSubtasksAndReminders
import com.example.database.model.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

@Dao
interface TaskWithRelationDao {
    @Query(
        """
    SELECT * FROM tasks
    WHERE (:includeDeleted OR syncStatus != 'DELETED')
      AND (:query == '' OR title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
    ORDER BY updatedAt DESC
""",
    )
    fun searchTasksWithRelations(
        query: String = "",
        includeDeleted: Boolean = false,
    ): Flow<List<TaskWithSubtasksAndReminders>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE (:includeDeleted OR syncStatus != 'DELETED')")
    fun getTasksWithSubtasksAndReminders(
        includeDeleted: Boolean = false,
    ): Flow<List<TaskWithSubtasksAndReminders>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId AND syncStatus != 'DELETED'")
    suspend fun getTaskWithSubtasks(taskId: Long): TaskWithSubtasks?

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId AND syncStatus != 'DELETED'")
    suspend fun getTaskWithSubtasksAndReminders(taskId: Long): TaskWithSubtasksAndReminders?

    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteSubtasksByTaskId(taskId: Long)

    @Query("DELETE FROM task_reminders WHERE taskId = :taskId")
    suspend fun deleteRemindersByTaskId(taskId: Long)

    @Query(
        """
    UPDATE tasks 
    SET isArchived = 1, 
        updatedAt = :now, 
        syncStatus = :syncStatus 
    WHERE id = :taskId
""",
    )
    suspend fun softDeleteTask(
        taskId: Long,
        now: Long,
        syncStatus: SyncStatus,
    )

    @Transaction
    suspend fun softDeleteTaskCascade(taskId: Long) {
        val now = Clock.System.now().toEpochMilliseconds()
        softDeleteTask(
            taskId,
            now,
            SyncStatus.DELETED,
        )
        deleteSubtasksByTaskId(taskId)
        deleteRemindersByTaskId(taskId)
    }
}
