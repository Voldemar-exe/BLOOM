package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.SubtaskEntity

@Dao
interface SubtaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subtaskEntity: SubtaskEntity)

    @Update
    suspend fun update(subtaskEntity: SubtaskEntity)

    @Transaction
    suspend fun upsertWithParentSync(
        subtaskEntity: SubtaskEntity,
        taskDao: TaskDao,
    ) {
        upsert(subtaskEntity)
        taskDao.updateSyncStatus(subtaskEntity.taskId)
    }

    @Transaction
    suspend fun updateWithParentSync(
        subtaskEntity: SubtaskEntity,
        taskDao: TaskDao,
    ) {
        update(subtaskEntity)
        taskDao.updateSyncStatus(subtaskEntity.taskId)
    }
}
