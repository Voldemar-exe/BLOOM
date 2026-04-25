package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.database.model.HabitEntity
import com.example.model.SyncStatus
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(habit: HabitEntity): Long

    @Update
    suspend fun update(habit: HabitEntity)

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
