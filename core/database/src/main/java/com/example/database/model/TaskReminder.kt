package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "task_reminders")
data class TaskReminder(
    @PrimaryKey val id: Long,
    val taskId: Long,
    val reminderTime: Instant,
    val isEnabled: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)
