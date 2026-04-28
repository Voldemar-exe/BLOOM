package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskEntity

data class TaskWithSubtasks(
    @Embedded val taskEntity: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val subtaskEntities: List<SubtaskEntity>
)