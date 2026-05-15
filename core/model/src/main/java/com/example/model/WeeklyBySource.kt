package com.example.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeeklyBySource(
    val habitsCompletions: List<Int>,
    val tasksCompletions: List<Int>,
    val achievementsUnlocks: List<Int>,
)