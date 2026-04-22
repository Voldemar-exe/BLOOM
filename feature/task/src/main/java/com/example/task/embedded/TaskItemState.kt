package com.example.task.embedded

import androidx.compose.runtime.Immutable
import com.example.model.Priority
import com.example.model.RecurrenceType
import com.example.model.Reminder
import com.example.model.Subtask

@Immutable
data class TaskItemState(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val daysOfWeek: Set<Int> = emptySet(),
    val deadline: Long? = 0,
    val reminders: List<Reminder> = emptyList(),
    val tags: List<String> = emptyList(),
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val isChecked: Boolean = false,
    val subtasks: List<Subtask> = emptyList(),
    val recurrenceType: RecurrenceType = RecurrenceType.WEEK,
    val hasEndDate: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
