package com.example.habit.embedded.item

import com.example.model.RecurrenceType
import com.example.model.Tag

sealed interface HabitSetupAction {
    data class OnTitleChange(val text: String) : HabitSetupAction

    data class OnDescriptionChange(val text: String) : HabitSetupAction

    data class OnTagClick(val tag: Tag) : HabitSetupAction

    data class SetRecurrenceType(val type: RecurrenceType) : HabitSetupAction

    data class UpdateSelectedDays(val days: Set<Int>) : HabitSetupAction

    data class AddReminder(val time: Pair<Int, Int>) : HabitSetupAction

    data class UpdateReminder(val index: Int, val time: Pair<Int, Int>) : HabitSetupAction

    data class ToggleReminder(val index: Int) : HabitSetupAction

    data class RemoveReminder(val index: Int) : HabitSetupAction

    data class AddStep(val title: String) : HabitSetupAction

    data class RemoveStep(val index: Int) : HabitSetupAction

    data class ToggleStep(val index: Int) : HabitSetupAction

//    data class SetPlant(val plant: HabitPlant) : HabitSetupAction

    object OnSaveHabit : HabitSetupAction

    data class LoadHabit(val habitId: Long?) : HabitSetupAction
}
