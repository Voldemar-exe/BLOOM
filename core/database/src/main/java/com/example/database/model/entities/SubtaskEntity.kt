package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity(tableName = "subtasks")
data class SubtaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val taskId: Long,
    val title: String,
    val isChecked: Boolean,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)
