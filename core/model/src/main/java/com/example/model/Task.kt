package com.example.model

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val daysOfMonth: List<Int>,
    val priority: Priority,
    val deadline: Long?,
    val tags: Set<Tag>,
    val isChecked: Boolean,
    val isArchived: Boolean,
    val isPaused: Boolean,
    val isMuted: Boolean
)
