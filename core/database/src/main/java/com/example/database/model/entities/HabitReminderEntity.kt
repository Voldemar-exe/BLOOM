package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity(tableName = "habit_reminders")
data class HabitReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val habitId: Long,
    val reminderTime: String,
    val isEnabled: Boolean,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)
