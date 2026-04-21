package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.Subtask
import com.example.database.model.Task

data class TaskAndSubtask(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val subtasks: List<Subtask>
)