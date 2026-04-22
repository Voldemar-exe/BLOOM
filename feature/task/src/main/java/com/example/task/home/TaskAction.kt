package com.example.task.home

import com.example.model.DayTimeInterval

sealed interface TaskAction {
    data class SelectTimeInterval(
        val timeInterval: DayTimeInterval,
    ) : TaskAction

    data class ToggleTask(
        val id: Int,
    ) : TaskAction

    data class ToggleSubtask(
        val id: Int,
    ) : TaskAction

    data object OnSearchClick : TaskAction

    data object OnAddClick : TaskAction

    data object OnDateClick : TaskAction

    data object OnFilterClick : TaskAction
}
