package com.example.task.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.TaskRepository
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
class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _taskUiState = MutableStateFlow(TaskState())
    val taskUiState: StateFlow<TaskState>
        get() = _taskUiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")
    private val filterTagsFlow = MutableStateFlow(emptySet<Tag>())

    init {
        Timber.d("Start with $taskUiState")
        viewModelScope.launch {
            searchQueryFlow
                .combine(filterTagsFlow) { query, tags -> query to tags }
                .flatMapLatest { (query, tags) ->
                    _taskUiState.update {
                        it.copy(
                            searchQuery = query,
                            selectedFilterTags = tags,
                        )
                    }
                    taskRepository.searchTasksWithRelations(query, tags)
                }.collect { tasks ->
                    Timber.d("Collected new tasks: $tasks")
                    _taskUiState.update { it.copy(tasks = tasks) }
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
        _taskUiState.update { it.copy(selectedTabTime = timeInterval) }
    }

    private fun handleToggleTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.toggleTask(taskId)
        }
    }

    private fun handleToggleSubtask(subtaskId: Long) {
        viewModelScope.launch {
            taskRepository.toggleSubtask(subtaskId)
        }
    }

    private fun handleTaskDeletion(taskId: Long) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

    private fun handleSearch(query: String) {
        searchQueryFlow.value = query.trim()
    }

    private fun handleTagSelect(tag: Tag) {
        filterTagsFlow.update { if (tag in it) it - tag else it + tag }
    }

    private fun handleDateSelection(dateRange: DateRange) {
        _taskUiState.update { it.copy(selectedDate = dateRange) }
    }
}
