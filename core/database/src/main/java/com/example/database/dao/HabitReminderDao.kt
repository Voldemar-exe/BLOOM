package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.SyncTypes
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.relationships.HabitWithReminders
import com.example.database.util.SyncTracker
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitReminderDao {
    @Query("SELECT * FROM habit_reminders WHERE habitId = :habitId")
    fun getReminders(habitId: Long): Flow<List<HabitReminderEntity>>

    @Transaction
    @Query("SELECT * FROM habits")
    fun getAllHabitsWithReminders(): Flow<List<HabitWithReminders>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reminder: HabitReminderEntity): Long

    @Update
    suspend fun update(reminder: HabitReminderEntity)

    @Query("DELETE FROM habit_reminders WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Transaction
    suspend fun upsertWithSync(
        reminder: HabitReminderEntity,
        tracker: SyncTracker,
    ) {
        val reminderId = upsert(reminder)
        tracker.trackSync(SyncTypes.HABIT_REMINDER, reminderId)
    }
}
