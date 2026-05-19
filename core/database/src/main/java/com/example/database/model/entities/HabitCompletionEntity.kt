package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_completions",
    indices = [
        Index(value = ["habitId", "createdAt"], unique = true),
    ],
)
data class HabitCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val completedAt: Long,
    val experienceEarned: Int,
    val coinsEarned: Int,
    val createdAt: Long = System.currentTimeMillis(),
)
