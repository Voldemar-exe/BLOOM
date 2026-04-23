package com.example.task.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.TaskRepository
import com.example.model.DayTimeInterval
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class TaskViewModel(
    val taskRepository: TaskRepository,
) : ViewModel() {
    private val _taskUiState = MutableStateFlow(TaskState())
    val taskUiState: StateFlow<TaskState>
        get() = _taskUiState.asStateFlow()

    init {
        Timber.d("Start with $taskUiState")
        viewModelScope.launch {
            taskRepository.getTasksWithRelations().collect { tasksWithRelations ->
                Timber.d("Colled new tasks: $tasksWithRelations")
                _taskUiState.update { it.copy(tasks = tasksWithRelations) }
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
}
