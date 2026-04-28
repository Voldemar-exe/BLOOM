package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity

data class HabitWithPlant(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId",
    )
    val plant: HabitPlantEntity,
)
