package com.example.habit.embedded.item

import androidx.compose.runtime.Immutable
import com.example.habit.util.toHabitPlant
import com.example.model.HabitPlant
import com.example.model.Recurrence
import com.example.model.RecurrenceType
import com.example.model.Reminder
import com.example.model.Tag
import com.example.plant.utils.PresetLibrary

@Immutable
data class HabitSetupState(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val recurrence: Recurrence = Recurrence(RecurrenceType.DAY, emptySet()),
    val tags: Set<Tag> = emptySet(),
    val steps: List<String> = emptyList(),
    val checkedSteps: Set<Int> = emptySet(),
    val reminders: List<Reminder> = emptyList(),
    val remindersToDelete: Set<Long> = emptySet(),
    val plant: HabitPlant = PresetLibrary.getExamples()[0].toHabitPlant(),
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val isChecked: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
