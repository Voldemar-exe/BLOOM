package com.example.model

import androidx.compose.runtime.Immutable

@Immutable
data class Subtask(
    val id: Long = 0,
    val taskId: Long,
    val title: String,
    val isChecked: Boolean = false,
)
