package com.example.task.home

import androidx.compose.runtime.Immutable
import com.example.model.DateRange
import com.example.model.DayTimeInterval
import com.example.model.Tag
import com.example.model.TaskWithRelations

@Immutable
data class TaskState(
    val tasks: List<TaskWithRelations> = emptyList(),
    val selectedTabTime: DayTimeInterval = DayTimeInterval.TODAY,
    val selectedDate: DateRange = DateRange(null, null),
    val searchQuery: String = "",
    val selectedFilterTags: Set<Tag> = emptySet(),
    val isLoading: Boolean = false,
    val error: String = "",
)