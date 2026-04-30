package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_completions")
data class HabitCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val completedAt: Long,
    val experienceEarned: Int,
    val coinsEarned: Int,
    val createdAt: Long = System.currentTimeMillis(),
)
