package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "task_completions",
    indices = [
        Index(value = ["taskId", "createdAt"], unique = true),
    ],
)
data class TaskCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long,
    val completedAt: Long,
    val experienceEarned: Int,
    val coinsEarned: Int,
    val createdAt: Long = System.currentTimeMillis(),
)
