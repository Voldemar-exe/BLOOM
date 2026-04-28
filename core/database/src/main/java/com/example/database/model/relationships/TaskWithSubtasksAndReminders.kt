package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskEntity
import com.example.database.model.entities.TaskReminderEntity

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
