package com.example.data.models

import java.util.UUID

data class Task(
    val taskId: UUID,
    val userId: UUID,
    val title: String,
    val description: String,
    val category: String, // TODO Add Enum about Categories
    val status: Boolean, // What is it? Why i need this?
    val priority: String, // TODO Add Enum about Priority
    val dueDate: Long,
    val createdAt: Long,
    val completedAt: Long,
    val isRecurring: Boolean,
    val recurrencePattern: String,  // TODO Add realization for pattern like Enum or Data Class,
    val tags: List<String>
)
