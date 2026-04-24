package com.example.task.embedded

import com.example.model.Priority
import com.example.model.RecurrenceType
import com.example.model.Tag

sealed interface TaskSetupAction {
    data class OnTitleChange(
        val text: String,
    ) : TaskSetupAction

    data class OnDescriptionChange(
        val text: String,
    ) : TaskSetupAction

    data class OnPriorityClick(
        val priority: Priority,
    ) : TaskSetupAction

    data class OnTagClick(
        val tag: Tag,
    ) : TaskSetupAction

    data class SetRecurrenceType(
        val type: RecurrenceType,
    ) : TaskSetupAction

    data class UpdateSelectedDays(
        val days: Set<Int>,
    ) : TaskSetupAction

    data class AddReminder(
        val time: Pair<Int, Int>,
    ) : TaskSetupAction

    data class UpdateReminder(
        val index: Int,
        val time: Pair<Int, Int>,
    ) : TaskSetupAction

    data class ToggleReminder(
        val index: Int,
    ) : TaskSetupAction

    data class RemoveReminder(
        val index: Int,
    ) : TaskSetupAction

    object ToggleEndDate : TaskSetupAction

    data class SetEndDate(
        val date: Long,
    ) : TaskSetupAction

    data class AddSubtask(
        val title: String,
    ) : TaskSetupAction

    data class RemoveSubtask(
        val index: Int,
    ) : TaskSetupAction

    data class ToggleSubtask(
        val index: Int,
    ) : TaskSetupAction

    object OnSaveTask : TaskSetupAction

    data class LoadTask(
        val taskId: Long?,
    ) : TaskSetupAction
}
