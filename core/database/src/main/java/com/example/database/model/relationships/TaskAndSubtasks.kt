package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.SubtaskEntity
import com.example.database.model.TaskEntity

data class TaskAndSubtasks(
    @Embedded val taskEntity: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val subtaskEntities: List<SubtaskEntity>
)