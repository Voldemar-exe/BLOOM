package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.HabitEntity
import com.example.database.model.HabitReminderEntity

data class HabitWithReminder(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId",
    )
    val habitReminder: List<HabitReminderEntity>,
)
