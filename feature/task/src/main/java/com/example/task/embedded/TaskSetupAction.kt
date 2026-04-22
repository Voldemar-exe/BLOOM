package com.example.task.embedded

import com.example.model.RecurrenceType

sealed interface TaskSetupAction {
    data class OnTitleChange(
        val text: String,
    ) : TaskSetupAction

    data class OnDescriptionChange(
        val text: String,
    ) : TaskSetupAction

    object OnPriorityClick : TaskSetupAction

    object OnTagsClick : TaskSetupAction

    data class SetRecurrenceType(
        val type: RecurrenceType,
    ) : TaskSetupAction

    data class ToggleDay(
        val dayIndex: Int,
    ) : TaskSetupAction

    data class SetReminderTime(
        val time: String,
    ) : TaskSetupAction

    object ToggleEndDate : TaskSetupAction

    data class SetEndDate(
        val date: String,
    ) : TaskSetupAction

    data class AddSubtask(
        val text: String,
    ) : TaskSetupAction

    data class RemoveSubtask(
        val index: Int,
    ) : TaskSetupAction

    data class ToggleSubtask(
        val index: Int,
    ) : TaskSetupAction

    object OnSaveTask : TaskSetupAction

    object OnNavigateBack : TaskSetupAction
}
