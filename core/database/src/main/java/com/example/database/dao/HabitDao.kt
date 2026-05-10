package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.database.model.SyncStatus
import com.example.database.model.entities.HabitEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

@Dao
interface HabitDao {
    @Query(
        """
        SELECT * FROM habits 
        WHERE syncStatus != 'DELETED'
        ORDER BY updatedAt DESC
    """,
    )
    fun getHabits(): Flow<List<HabitEntity>>

    @Query(
        """
        SELECT * FROM habits 
        WHERE id = :habitId AND syncStatus != 'DELETED'
    """,
    )
    suspend fun getHabitById(habitId: Long): HabitEntity?

    @Query(
        """
        UPDATE habits 
        SET syncStatus = :syncStatus 
        WHERE id = :habitId
    """,
    )
    suspend fun updateSyncStatus(
        habitId: Long,
        syncStatus: SyncStatus = SyncStatus.CHANGED,
    )

    @Query(
        """
        UPDATE habits 
        SET isChecked = :isChecked,
            updatedAt = :now,
            syncStatus = :syncStatus
        WHERE id = :habitId
    """,
    )
    suspend fun updateHabitCompletion(
        habitId: Long,
        isChecked: Boolean,
        now: Long = Clock.System.now().toEpochMilliseconds(),
        syncStatus: SyncStatus = SyncStatus.CHANGED,
    )
}
