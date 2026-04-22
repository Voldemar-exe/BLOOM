package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.database.model.TaskEntity
import com.example.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY updatedAt DESC")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(taskEntity: TaskEntity): Long

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Query("UPDATE tasks SET syncStatus = :syncStatus WHERE id = :taskId")
    suspend fun updateSyncStatus(
        taskId: Long,
        syncStatus: SyncStatus = SyncStatus.CHANGED,
    )
}
