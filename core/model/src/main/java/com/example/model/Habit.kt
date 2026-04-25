package com.example.model

data class Habit(
    val id: Long,
    val title: String,
    val description: String,
    val daysOfWeek: List<Int>,
    val tags: Set<Tag>,
    val steps: List<String>,
    val isChecked: Boolean,
    val isArchived: Boolean,
    val isPaused: Boolean,
    val isMuted: Boolean,
)
