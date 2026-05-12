package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.SyncOperation
import com.example.database.model.SyncStatus
import com.example.database.model.SyncTypes
import com.example.database.model.entities.HabitEntity
import com.example.database.model.relationships.HabitWithPlant
import com.example.database.model.relationships.HabitWithPlantAndReminders
import com.example.database.util.SyncTracker
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
    @Query("SELECT * FROM habits WHERE id = :habitId AND syncStatus != 'DELETED'")
    suspend fun getHabitWithPlantAndReminders(habitId: Long): HabitWithPlantAndReminders?

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
    suspend fun softDeleteHabitCascade(
        habit: HabitEntity,
        tracker: SyncTracker,
    ) {
        val now = Clock.System.now().toEpochMilliseconds()

        softDeleteHabit(habit.id, now, SyncStatus.DELETED)
        tracker.trackSync(SyncTypes.HABIT, habit.id, SyncOperation.DELETE)
        deletePlantByHabitId(habit.id)
        deleteRemindersByHabitId(habit.id)
    }
}
