package com.example.model

import androidx.compose.runtime.Immutable

@Immutable
data class Habit(
    val id: Long,
    val title: String,
    val description: String,
    val recurrence: Recurrence,
    val tags: Set<Tag>,
    val steps: List<String>,
    val isChecked: Boolean,
    val isArchived: Boolean,
    val isPaused: Boolean,
    val isMuted: Boolean,
)
