package com.example.task.home

import com.example.model.DayTimeInterval

sealed interface TaskAction {
    data class SelectTimeInterval(
        val timeInterval: DayTimeInterval,
    ) : TaskAction

    data class ToggleTask(
        val id: Long,
    ) : TaskAction

    data class ToggleSubtask(
        val id: Long,
    ) : TaskAction
}
