package com.example.model

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val daysOfWeek: List<Int>,
    val priority: Priority,
    val deadline: Long?,
    val tags: List<String>,
    val isArchived: Boolean,
    val isPaused: Boolean,
    val isMuted: Boolean
)
