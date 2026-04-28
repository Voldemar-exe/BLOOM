package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.database.model.SyncStatus
import kotlin.time.Clock

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val daysOfWeek: List<Int>,
    val tags: List<String>,
    val steps: List<String>,
    val isChecked: Boolean = false,
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val startAt: Long = Clock.System.now().toEpochMilliseconds(),
    val endAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val syncStatus: SyncStatus = SyncStatus.CHANGED
)
