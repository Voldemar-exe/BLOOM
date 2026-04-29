package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.entities.TaskEntity
import com.example.database.model.entities.TaskReminderEntity

data class TaskWithReminders(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val reminders: List<TaskReminderEntity>,
)
