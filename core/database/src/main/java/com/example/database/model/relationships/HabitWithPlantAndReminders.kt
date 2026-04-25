package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.HabitEntity
import com.example.database.model.HabitPlantEntity
import com.example.database.model.HabitReminderEntity

data class HabitWithPlantAndReminders(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId",
    )
    val plant: HabitPlantEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId",
    )
    val habitReminders: List<HabitReminderEntity>,
)
