package com.example.model

import androidx.compose.runtime.Immutable

@Immutable
data class Subtask(
    val title: String,
    val isChecked: Boolean = false,
)
