package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.SubtaskEntity

@Dao
interface SubtaskDao {
    @Query("SELECT * FROM subtasks WHERE id = :subtaskId")
    suspend fun findById(subtaskId: Long): SubtaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subtaskEntity: SubtaskEntity)

    @Update
    suspend fun update(subtaskEntity: SubtaskEntity)

    @Query("SELECT COUNT(*) FROM subtasks WHERE taskId = :taskId")
    suspend fun countByTaskId(taskId: Long): Int

    @Query("SELECT COUNT(*) FROM subtasks WHERE taskId = :taskId AND isChecked = 1")
    suspend fun countCheckedByTaskId(taskId: Long): Int

    @Transaction
    suspend fun upsertWithParentSync(
        subtaskEntity: SubtaskEntity,
        taskDao: TaskDao,
    ) {
        upsert(subtaskEntity)
        updateParentTaskCompletion(taskDao, subtaskEntity.taskId)
    }

    @Transaction
    suspend fun updateWithParentSync(
        subtaskEntity: SubtaskEntity,
        taskDao: TaskDao,
    ) {
        update(subtaskEntity)
        updateParentTaskCompletion(taskDao, subtaskEntity.taskId)
    }

    private suspend fun updateParentTaskCompletion(
        taskDao: TaskDao,
        taskId: Long,
    ) {
        val total = countByTaskId(taskId)
        val checked = countCheckedByTaskId(taskId)
        val isComplete = total > 0 && checked == total
        taskDao.updateTaskCompletionAndSync(taskId, isComplete)
    }
}
