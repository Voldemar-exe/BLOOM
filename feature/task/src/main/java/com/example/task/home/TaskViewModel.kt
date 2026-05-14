package com.example.task.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.TaskRepository
import com.example.model.DateRange
import com.example.model.DayTimeInterval
import com.example.model.FilterParams
import com.example.model.Tag
import com.example.task.usecases.CompleteTaskUseCase
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
class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val completeTaskUseCase: CompleteTaskUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState>
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
        Timber.d("Start with $state")

        viewModelScope.launch {
            filtersFlow
                .flatMapLatest { filters ->
                    taskRepository.searchTasksWithRelations(
                        query = filters.query,
                        filterTags = filters.tags,
                        timeInterval = filters.tabTime,
                        dateRange = filters.dateRange,
                    )
                }.collect { tasks ->
                    Timber.d("Collected new tasks: $tasks")
                    _state.update { it.copy(tasks = tasks) }
                }
        }
    }

    fun onAction(action: TaskAction) {
        Timber.d("$action")
        when (action) {
            is TaskAction.SelectTimeInterval -> handleSelectTimeInterval(action.timeInterval)
            is TaskAction.ToggleTask -> handleToggleTask(action.id)
            is TaskAction.ToggleSubtask -> handleToggleSubtask(action.id)
            is TaskAction.DeleteTask -> handleTaskDeletion(action.id)
            is TaskAction.Search -> handleSearch(action.query)
            is TaskAction.OnTagSelect -> handleTagSelect(action.tag)
            is TaskAction.SelectDateRange -> handleDateSelection(action.dateRange)
        }
    }

    private fun handleSelectTimeInterval(timeInterval: DayTimeInterval) {
        _state.update { it.copy(selectedTabTime = timeInterval) }
    }

    private fun handleToggleTask(taskId: Long) {
        viewModelScope.launch {
            completeTaskUseCase(taskId)
        }
    }

    private fun handleToggleSubtask(subtaskId: Long) {
        viewModelScope.launch {
            val taskId = taskRepository.toggleSubtask(subtaskId)
            if (taskId != null) completeTaskUseCase(taskId = taskId, calledBySubtask = true)
        }
    }

    private fun handleTaskDeletion(taskId: Long) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
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
