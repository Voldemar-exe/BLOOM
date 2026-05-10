package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.model.SyncTypes
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.util.SyncTracker

@Dao
interface HabitPlantDao {
    @Query("SELECT * FROM habit_plants WHERE habitId = :habitId")
    suspend fun getByHabitId(habitId: Long): HabitPlantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabit(entity: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlant(entity: HabitPlantEntity)

    @Transaction
    suspend fun upsertHabitWithPlant(
        habit: HabitEntity,
        plant: HabitPlantEntity,
        tracker: SyncTracker,
    ): Long {
        val habitId = upsertHabit(habit)
        upsertPlant(plant.copy(habitId = habitId))
        tracker.trackUpsert(SyncTypes.HABIT, habitId, habit)
        return habitId
    }
}
