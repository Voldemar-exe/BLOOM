package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.database.model.SyncStatus
import com.example.model.Priority
import com.example.model.Recurrence
import kotlin.time.Clock

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val recurrence: Recurrence,
    val priority: Priority,
    val deadline: Long?,
    val tags: List<String>,
    val isChecked: Boolean = false,
    val isArchived: Boolean,
    val isPaused: Boolean,
    val isMuted: Boolean,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val syncStatus: SyncStatus = SyncStatus.CHANGED,
)
