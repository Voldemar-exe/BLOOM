package com.example.habit.home

import com.example.model.DateRange
import com.example.model.DayTimeInterval
import com.example.model.Tag

sealed interface HabitAction {
    data class SelectTimeInterval(val timeInterval: DayTimeInterval) : HabitAction

    data class SelectDateRange(val dateRange: DateRange) : HabitAction

    data class Search(val query: String) : HabitAction

    data class OnTagSelect(val tag: Tag) : HabitAction

    data class ToggleHabit(val id: Long) : HabitAction

    data class DeleteHabit(val id: Long) : HabitAction
}
