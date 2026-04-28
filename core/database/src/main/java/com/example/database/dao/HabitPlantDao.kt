package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity

@Dao
interface HabitPlantDao {
    @Query("SELECT * FROM habit_plants WHERE habitId = :habitId")
    suspend fun getByHabitId(habitId: Long): HabitPlantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: HabitPlantEntity): Long

    @Update
    suspend fun update(entity: HabitPlantEntity)

    @Query("DELETE FROM habit_plants WHERE habitId = :habitId")
    suspend fun deleteByHabitId(habitId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabit(entity: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlant(entity: HabitPlantEntity)

    @Transaction
    suspend fun upsertHabitWithPlant(
        habit: HabitEntity,
        plant: HabitPlantEntity,
    ): Long {
        val habitId = upsertHabit(habit)
        upsertPlant(plant.copy(habitId = habitId))
        return habitId
    }
}
