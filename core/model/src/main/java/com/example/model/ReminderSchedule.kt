package com.example.model

import java.time.LocalTime

data class ReminderSchedule(
    val id: Long,
    val parentId: Long,
    val title: String,
    val description: String,
    val time: LocalTime,
    val recurrence: Recurrence,
    val isEnabled: Boolean,
    val type: ReminderType,
) {
    val notificationTitle: String
        get() =
            when (type) {
                ReminderType.HABIT -> "Привычка: $title"
                ReminderType.TASK -> "Задача: $title"
            }

    val notificationText: String
        get() =
            description.ifBlank {
                when (type) {
                    ReminderType.HABIT -> "Пора выполнить привычку"
                    ReminderType.TASK -> "Пора выполнить задачу"
                }
            }
}
