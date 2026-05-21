package com.example.habit.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.HabitRepository
import com.example.habit.usecases.CompleteHabitUseCase
import com.example.habit.usecases.GetHabitsCompletionsUseCase
import com.example.model.DateRange
import com.example.model.DayTimeInterval
import com.example.model.FilterParams
import com.example.model.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class HabitViewModel(
    private val habitRepository: HabitRepository,
    private val completeHabitUseCase: CompleteHabitUseCase,
    private val getHabitsCompletionsUseCase: GetHabitsCompletionsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(HabitsState())
    val state: StateFlow<HabitsState>
        get() = _state.asStateFlow()

    private val filtersFlow =
        state
            .map { state ->
                FilterParams(
                    query = state.searchQuery,
                    tags = state.selectedFilterTags,
                    tabTime = state.selectedTabTime,
                    dateRange = state.selectedDate,
                )
            }.distinctUntilChanged()

    init {
        Timber.d("Start with ${_state.value}")

        viewModelScope.launch {
            filtersFlow
                .flatMapLatest { filters ->
                    habitRepository.searchHabitsWithRelations(
                        query = filters.query,
                        filterTags = filters.tags,
                        timeInterval = filters.tabTime,
                        dateRange = filters.dateRange,
                    )
                }.distinctUntilChanged()
                .collect { habits ->
                    Timber.d("Collected habits: $habits")
                    _state.update {
                        it.copy(habits = habits)
                    }
                }
        }
        viewModelScope.launch {
            getHabitsCompletionsUseCase()
                .distinctUntilChanged()
                .collect { completions ->
                    Timber.d("Collected completions: $completions")
                    _state.update { it.copy(completions = completions) }
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
        _state.update { it.copy(selectedTabTime = timeInterval) }
    }

    private fun handleToggleHabit(habitId: Long) {
        viewModelScope.launch {
            completeHabitUseCase(habitId)
        }
    }

    private fun handleDeleteHabit(habitId: Long) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habitId)
        }
    }

    private fun handleSearch(query: String) {
        _state.update { it.copy(searchQuery = query.trim()) }
    }

    private fun handleTagSelect(tag: Tag) {
        _state.update { state ->
            state.copy(
                selectedFilterTags =
                    if (tag in state.selectedFilterTags) {
                        state.selectedFilterTags - tag
                    } else {
                        state.selectedFilterTags + tag
                    },
            )
        }
    }

    private fun handleDateSelection(dateRange: DateRange) {
        _state.update { it.copy(selectedDate = dateRange) }
    }
}
