package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Priority
import com.example.model.SyncStatus

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: Long,
    val userId: Long,
    val title: String,
    val description: String,
    val daysOfWeek: List<Int>, // TODO: Converter
    val priority: Priority,
    val deadline: Long?,
    val tags: List<String>, // TODO: Converter
    val isArchived: Boolean,
    val isPaused: Boolean,
    val isMuted: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus,
)
