package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.relationships.HabitWithPlant
import com.example.database.model.relationships.HabitWithPlantAndReminders
import com.example.model.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

@Dao
interface HabitWithRelationDao {
    @Transaction
    @Query(
        """
    SELECT * FROM habits
    WHERE (:includeDeleted OR syncStatus != 'DELETED')
      AND (:query == '' OR title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
    ORDER BY updatedAt DESC
""",
    )
    fun searchHabitsWithRelations(
        query: String = "",
        includeDeleted: Boolean = false,
    ): Flow<List<HabitWithPlantAndReminders>>

    @Transaction
    @Query(
        """
        SELECT * FROM habits
        WHERE (:includeDeleted OR syncStatus != 'DELETED')
        ORDER BY updatedAt DESC
    """,
    )
    fun getHabitsWithPlant(includeDeleted: Boolean = false): Flow<List<HabitWithPlant>>

    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId AND syncStatus != 'DELETED'")
    suspend fun getHabitWithPlantAndReminders(habitId: Long): HabitWithPlantAndReminders?

    @Transaction
    @Query(
        """
        SELECT * FROM habits
        WHERE id = :habitId AND syncStatus != 'DELETED'
    """,
    )
    suspend fun getHabitWithPlant(habitId: Long): HabitWithPlant?

    @Transaction
    @Query(
        """
        SELECT * FROM habits
        WHERE (:includeDeleted OR syncStatus != 'DELETED')
        AND (:query == '' OR title LIKE '%' || :query || '%' 
             OR description LIKE '%' || :query || '%')
        ORDER BY updatedAt DESC
    """,
    )
    fun searchHabitsWithPlant(
        query: String = "",
        includeDeleted: Boolean = false,
    ): Flow<List<HabitWithPlant>>

    @Query(
        """
        UPDATE habits 
        SET isArchived = 1,
            updatedAt = :now,
            syncStatus = :syncStatus
        WHERE id = :habitId
    """,
    )
    suspend fun softDeleteHabit(
        habitId: Long,
        now: Long,
        syncStatus: SyncStatus,
    )

    @Query("DELETE FROM habit_plants WHERE habitId = :habitId")
    suspend fun deletePlantByHabitId(habitId: Long)

    @Query("DELETE FROM habit_reminders WHERE habitId = :habitId")
    suspend fun deleteRemindersByHabitId(habitId: Long)

    @Transaction
    suspend fun softDeleteHabitCascade(habitId: Long) {
        val now = Clock.System.now().toEpochMilliseconds()

        softDeleteHabit(habitId, now, SyncStatus.DELETED)
        deletePlantByHabitId(habitId)
        deleteRemindersByHabitId(habitId)
    }
}
