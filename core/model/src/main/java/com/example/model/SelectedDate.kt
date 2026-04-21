package com.example.model

import androidx.compose.runtime.Immutable

@Immutable
data class SelectedDate(
    val startDate: Long = 0,
    val endDate: Long = 0
)
