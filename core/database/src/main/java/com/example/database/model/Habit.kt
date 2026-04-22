package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.SyncStatus
import kotlin.time.Instant

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val tags: List<String>,
    val steps: List<String>,
    val daysOfWeek: List<Int>,
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val startAt: Long?, // Date to show
    val endAt: Long?, // Date to show
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val syncStatus: SyncStatus = SyncStatus.CHANGED
)
