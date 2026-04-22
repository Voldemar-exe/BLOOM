package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "habit_reminders")
data class HabitReminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val habitId: Long,
    val reminderTime: Instant,
    val isEnabled: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)
