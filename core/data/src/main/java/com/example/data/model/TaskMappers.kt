package com.example.data.model

import com.example.database.model.SubtaskEntity
import com.example.database.model.TaskEntity
import com.example.database.model.TaskReminderEntity
import com.example.database.model.relationships.TaskWithSubtasksAndReminders
import com.example.model.Reminder
import com.example.model.Subtask
import com.example.model.Tag
import com.example.model.Task
import com.example.model.TaskWithRelations
import java.time.LocalTime

fun TaskEntity.asModel() =
    Task(
        id = id,
        title = title,
        description = description,
        daysOfWeek = daysOfWeek,
        priority = priority,
        deadline = deadline,
        tags = tags.map { Tag.valueOf(it) }.toSet(),
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
    )

fun Task.asEntity() =
    TaskEntity(
        id = id,
        title = title,
        description = description,
        daysOfWeek = daysOfWeek,
        priority = priority,
        deadline = deadline,
        tags = tags.map { it.name },
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
    )

fun SubtaskEntity.asModel() =
    Subtask(
        id = id,
        taskId = taskId,
        title = title,
        isChecked = isChecked,
    )

fun Subtask.asEntity() =
    SubtaskEntity(
        id = id,
        taskId = taskId,
        title = title,
        isChecked = isChecked,
    )

fun TaskReminderEntity.asModel() =
    Reminder(
        id = id,
        parentId = taskId,
        time = stringToLocalTime(reminderTime),
        isEnabled = isEnabled,
    )

private fun stringToLocalTime(time: String): LocalTime {
    val pairTime = time.split(":").map { it.toInt() }
    return LocalTime.of(pairTime[0], pairTime[1])
}

fun Reminder.asTaskEntity() =
    TaskReminderEntity(
        id = id,
        taskId = parentId,
        reminderTime = time.hour.timeToTwoNumbers() + ":" + time.minute.timeToTwoNumbers(),
        isEnabled = isEnabled,
    )

private fun Int.timeToTwoNumbers() = if (this.toString().length > 1) this.toString() else "0$this"

fun TaskWithSubtasksAndReminders.asModel(): TaskWithRelations =
    TaskWithRelations(
        task = taskEntity.asModel(),
        subtask = subtaskEntities.map { it.asModel() },
        reminders = taskReminderEntities.map { it.asModel() },
    )
