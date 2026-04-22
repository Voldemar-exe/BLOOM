package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.SubtaskEntity
import com.example.database.model.TaskEntity
import com.example.database.model.TaskReminderEntity

data class TaskWithSubtasksAndReminders(
    @Embedded val taskEntity: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val subtaskEntities: List<SubtaskEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val taskReminderEntities: List<TaskReminderEntity>,
)
