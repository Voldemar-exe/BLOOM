package com.example.model

data class TaskWithRelations(
    val task: Task,
    val subtask: List<Subtask>,
    val reminders: List<Reminder>
)
