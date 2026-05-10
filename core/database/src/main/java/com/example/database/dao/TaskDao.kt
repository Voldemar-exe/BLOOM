package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.SyncStatus
import com.example.database.model.SyncTypes
import com.example.database.model.entities.TaskEntity
import com.example.database.util.SyncTracker
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE syncStatus != 'DELETED' ORDER BY updatedAt DESC")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId AND syncStatus != 'DELETED'")
    suspend fun getTaskById(taskId: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(taskEntity: TaskEntity): Long

    @Transaction
    suspend fun upsertWithSync(
        taskEntity: TaskEntity,
        tracker: SyncTracker,
    ): Long {
        val taskId = upsert(taskEntity)
        tracker.trackUpsert(SyncTypes.TASK, taskId, taskEntity)
        return taskId
    }

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Query("UPDATE tasks SET syncStatus = :syncStatus WHERE id = :taskId")
    suspend fun updateSyncStatus(
        taskId: Long,
        syncStatus: SyncStatus = SyncStatus.CHANGED,
    )

    @Query(
        """
        UPDATE tasks SET isChecked = :isComplete,
        updatedAt = :now,
        syncStatus = :syncStatus
        WHERE id = :taskId
        """,
    )
    suspend fun updateTaskCompletionAndSync(
        taskId: Long,
        isComplete: Boolean,
        now: Long = Clock.System.now().toEpochMilliseconds(),
        syncStatus: SyncStatus = SyncStatus.CHANGED,
    )

    @Query(
        """
    UPDATE subtasks
    SET isChecked = :isChecked,
        updatedAt = :now
    WHERE taskId = :taskId
""",
    )
    suspend fun updateSubtasksCheckState(
        taskId: Long,
        isChecked: Boolean,
        now: Long,
    )

    @Transaction
    suspend fun toggleTaskWithSubtasks(taskId: Long) {
        val task = getTaskById(taskId) ?: return
        val newCheckState = !task.isChecked
        val now = Clock.System.now().toEpochMilliseconds()

        update(
            task.copy(
                isChecked = newCheckState,
                updatedAt = now,
                syncStatus = SyncStatus.CHANGED,
            ),
        )
        updateSubtasksCheckState(taskId, newCheckState, now)
    }
}
