package com.example.task.home

import com.example.model.DateRange
import com.example.model.DayTimeInterval
import com.example.model.Tag

sealed interface TaskAction {
    data class SelectTimeInterval(val timeInterval: DayTimeInterval) : TaskAction

    data class SelectDateRange(val dateRange: DateRange) : TaskAction

    data class Search(val query: String) : TaskAction

    data class OnTagSelect(val tag: Tag) : TaskAction

    data class ToggleTask(val id: Long) : TaskAction

    data class ToggleSubtask(val id: Long) : TaskAction

    data class DeleteTask(val id: Long) : TaskAction
}
