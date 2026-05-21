package com.example.task.embedded

import androidx.compose.runtime.Immutable
import com.example.model.Priority
import com.example.model.Recurrence
import com.example.model.RecurrenceType
import com.example.model.Reminder
import com.example.model.Subtask
import com.example.model.Tag

@Immutable
data class TaskSetupState(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val recurrence: Recurrence = Recurrence(RecurrenceType.DAY, emptySet()),
    val deadline: Long? = null,
    val reminders: List<Reminder> = emptyList(),
    val remindersToDelete: Set<Long> = emptySet(),
    val tags: Set<Tag> = emptySet(),
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val isChecked: Boolean = false,
    val subtasks: List<Subtask> = emptyList(),
    val subtasksToDelete: Set<Long> = emptySet(),
    val hasEndDate: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
