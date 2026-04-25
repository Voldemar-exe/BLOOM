package com.example.habit.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.HabitRepository
import com.example.model.DateRange
import com.example.model.DayTimeInterval
import com.example.model.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class HabitViewModel(private val habitRepository: HabitRepository) : ViewModel() {
    private val _habitState = MutableStateFlow(HabitState())
    val habitState: StateFlow<HabitState>
        get() = _habitState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")
    private val filterTagsFlow = MutableStateFlow(emptySet<Tag>())

    init {
        Timber.d("Start with ${_habitState.value}")

        viewModelScope.launch {
            searchQueryFlow
                .combine(filterTagsFlow) { query, tags -> query to tags }
                .flatMapLatest { (query, tags) ->
                    _habitState.update {
                        it.copy(
                            searchQuery = query,
                            selectedFilterTags = tags,
                        )
                    }

                    habitRepository.searchHabitsWithRelations(query, tags)
                }.collect { habits ->
                    Timber.d("Collected habits: $habits")
                    _habitState.update { it.copy(habits = habits) }
                }
        }
    }

    fun onAction(action: HabitAction) {
        Timber.d("$action")

        when (action) {
            is HabitAction.SelectTimeInterval -> handleSelectTimeInterval(action.timeInterval)
            is HabitAction.ToggleHabit -> handleToggleHabit(action.id)
            is HabitAction.DeleteHabit -> handleDeleteHabit(action.id)
            is HabitAction.Search -> handleSearch(action.query)
            is HabitAction.OnTagSelect -> handleTagSelect(action.tag)
            is HabitAction.SelectDateRange -> handleDateSelection(action.dateRange)
        }
    }

    private fun handleSelectTimeInterval(timeInterval: DayTimeInterval) {
        _habitState.update { it.copy(selectedTabTime = timeInterval) }
    }

    private fun handleToggleHabit(habitId: Long) {
        viewModelScope.launch {
            habitRepository.toggleHabit(habitId)
        }
    }

    private fun handleDeleteHabit(habitId: Long) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habitId)
        }
    }

    private fun handleSearch(query: String) {
        searchQueryFlow.value = query.trim()
    }

    private fun handleTagSelect(tag: Tag) {
        filterTagsFlow.update {
            if (tag in it) it - tag else it + tag
        }
    }

    private fun handleDateSelection(dateRange: DateRange) {
        _habitState.update { it.copy(selectedDate = dateRange) }
    }
}
