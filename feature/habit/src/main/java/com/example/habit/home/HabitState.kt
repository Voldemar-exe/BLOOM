package com.example.habit.home

import androidx.compose.runtime.Immutable
import com.example.model.DateRange
import com.example.model.DayTimeInterval
import com.example.model.HabitCompletion
import com.example.model.HabitWithRelations
import com.example.model.Tag

@Immutable
data class HabitState(
    val habits: List<HabitWithRelations> = emptyList(),
    val selectedTabTime: DayTimeInterval = DayTimeInterval.TODAY,
    val selectedDate: DateRange = DateRange(null, null),
    val searchQuery: String = "",
    val selectedFilterTags: Set<Tag> = emptySet(),
    val completions: List<HabitCompletion> = emptyList(),
) {
    val completionCounts: Map<Long, Int> by lazy {
        completions.groupingBy { it.habitId }.eachCount()
    }
}
