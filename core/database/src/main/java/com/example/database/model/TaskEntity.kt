package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Priority
import com.example.model.SyncStatus
import kotlin.time.Clock

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val daysOfWeek: List<Int>,
    val priority: Priority,
    val deadline: Long?,
    val tags: List<String>,
    val isArchived: Boolean,
    val isPaused: Boolean,
    val isMuted: Boolean,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val syncStatus: SyncStatus = SyncStatus.CHANGED,
)
