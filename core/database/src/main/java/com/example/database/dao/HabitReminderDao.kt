package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.relationships.HabitWithReminders
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitReminderDao {
    @Query("SELECT * FROM habit_reminders WHERE habitId = :habitId")
    fun getReminders(habitId: Long): Flow<List<HabitReminderEntity>>

    @Transaction
    @Query("SELECT * FROM habits")
    fun getAllHabitsWithReminders(): Flow<List<HabitWithReminders>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reminder: HabitReminderEntity)

    @Update
    suspend fun update(reminder: HabitReminderEntity)

    @Query("DELETE FROM habit_reminders WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Transaction
    suspend fun upsertWithParentSync(
        reminder: HabitReminderEntity,
        habitDao: HabitDao,
    ) {
        upsert(reminder)
        habitDao.updateSyncStatus(reminder.habitId)
    }

    @Transaction
    suspend fun updateWithParentSync(
        reminder: HabitReminderEntity,
        habitDao: HabitDao,
    ) {
        update(reminder)
        habitDao.updateSyncStatus(reminder.habitId)
    }
}
