package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.Habit
import com.example.database.model.HabitPlant

data class HabitAndHabitPlant(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId",
    )
    val habitPlant: HabitPlant,
)
