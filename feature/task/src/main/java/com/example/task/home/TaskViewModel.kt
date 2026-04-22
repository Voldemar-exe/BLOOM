package com.example.task.home

import androidx.lifecycle.ViewModel
import com.example.data.repository.TaskRepository
import com.example.model.DayTimeInterval
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class TaskViewModel(
    taskRepository: TaskRepository
) : ViewModel() {
    private val _taskUiState = MutableStateFlow(TaskState())
    val taskUiState: StateFlow<TaskState>
        get() = _taskUiState.asStateFlow()

    init {
        Timber.d("Start with $taskUiState")
    }

    fun onAction(action: TaskAction) {
        when (action) {
            TaskAction.OnAddClick -> handleAddClick()
            TaskAction.OnDateClick -> handleDateClick()
            TaskAction.OnSearchClick -> handleSearchClick()
            TaskAction.OnFilterClick -> handleFilterClick()
            is TaskAction.SelectTimeInterval -> handleSelectTimeInterval(action.timeInterval)
            is TaskAction.ToggleTask -> handleToggleTask(action.id)
            is TaskAction.ToggleSubtask -> handleToggleSubtask(action.id)
        }
    }

    private fun handleAddClick() {
        Timber.d("Add task clicked")
    }

    private fun handleDateClick() {
        Timber.d("Date picker clicked")
    }

    private fun handleSearchClick() {
        Timber.d("Search clicked")
    }

    private fun handleFilterClick() {
        Timber.d("Sort clicked")
    }

    private fun handleSelectTimeInterval(timeInterval: DayTimeInterval) {
        _taskUiState.update { it.copy(selectedTabTime = timeInterval) }
        // viewModelScope.launch { loadTasksForInterval(timeInterval) }
    }

    private fun handleToggleTask(id: Int) {
//        _taskUiState.update { currentState ->
//            val updatedTasks =
//                currentState.tasks.mapKeys { (task, _) ->
//                    if (task.id == id) task.copy(isChecked = !task.isChecked) else task
//                }
//            currentState.copy(tasks = updatedTasks)
//        }
    }

    private fun handleToggleSubtask(id: Int) {
//        _taskUiState.update { currentState ->
//            val updatedTasks =
//                currentState.tasks.mapKeys { (task, _) ->
//                    if (task.id == id) task.copy(isChecked = !task.isChecked) else task
//                }
//            currentState.copy(tasks = updatedTasks)
//        }
    }

//     private suspend fun loadTasksForInterval(interval: DayTimeInterval) {
//         _taskUiState.update { it.copy(isLoading = true) }
//         try {
//             val tasks = taskRepository.getTasksForInterval(interval)
//             _taskUiState.update { it.copy(tasks = tasks, isLoading = false) }
//         } catch (e: Exception) {
//             _taskUiState.update { it.copy(error = e.message ?: "Unknown error", isLoading = false) }
//             Timber.e(e, "Failed to load tasks for interval: $interval")
//         }
//     }
}
