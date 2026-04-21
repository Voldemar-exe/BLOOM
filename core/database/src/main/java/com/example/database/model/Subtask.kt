package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtasks")
data class Subtask(
    @PrimaryKey val id: Long,
    val taskId: Long,
    val title: String,
    val isChecked: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)
