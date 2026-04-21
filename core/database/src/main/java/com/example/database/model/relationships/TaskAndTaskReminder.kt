package com.example.database.model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.model.Task
import com.example.database.model.TaskReminder

data class TaskAndTaskReminder(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val taskReminders: List<TaskReminder>
)