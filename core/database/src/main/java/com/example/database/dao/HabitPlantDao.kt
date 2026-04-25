package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.database.model.HabitPlantEntity

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
}
