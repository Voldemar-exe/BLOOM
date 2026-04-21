package com.example.task

import com.example.model.DayTimeInterval
import com.example.model.Priority
import com.example.model.SelectedDate

data class TaskState(
    val tasks: Map<TaskItemState, List<SubTask>> = emptyMap(),
    val selectedTabTime: DayTimeInterval = DayTimeInterval.TODAY,
    val selectedDate: SelectedDate = SelectedDate(0, 0),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
)

data class TaskItemState(
    val id: Int,
    val title: String,
    val description: String? = null,
    val daysOfWeek: List<Int> = emptyList(),
    val priority: List<Priority> = emptyList(),
    val deadline: Long = 0,
    val tags: List<String> = emptyList(),
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val isChecked: Boolean = false,
)

data class SubTask(
    val title: String,
    val isChecked: Boolean = false,
)
