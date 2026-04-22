package com.example.task.home

import androidx.compose.runtime.Immutable
import com.example.model.DayTimeInterval
import com.example.model.SelectedDate
import com.example.model.TaskWithRelations

@Immutable
data class TaskState(
    val tasks: List<TaskWithRelations> = emptyList(),
    val selectedTabTime: DayTimeInterval = DayTimeInterval.TODAY,
    val selectedDate: SelectedDate = SelectedDate(0, 0),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
)