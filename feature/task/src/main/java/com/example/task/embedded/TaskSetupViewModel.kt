package com.example.task.embedded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.TaskRepository
import com.example.model.RecurrenceType
import com.example.model.Subtask
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class TaskSetupViewModel(
    taskRepository: TaskRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TaskItemState())
    val state: StateFlow<TaskItemState> = _state.asStateFlow()

    private val _effect =
        MutableSharedFlow<TaskItemEffect>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.SUSPEND,
        )
    val effect: SharedFlow<TaskItemEffect> = _effect

    fun onAction(action: TaskSetupAction) {
        when (action) {
            is TaskSetupAction.OnTitleChange -> updateTitle(action.text)
            is TaskSetupAction.OnDescriptionChange -> updateDescription(action.text)
            TaskSetupAction.OnPriorityClick -> Timber.d("Open priority")
            TaskSetupAction.OnTagsClick -> Timber.d("Open tags")
            is TaskSetupAction.SetRecurrenceType -> setRecurrence(action.type)
            is TaskSetupAction.ToggleDay -> toggleDay(action.dayIndex)
            is TaskSetupAction.SetReminderTime -> { /* TODO */ }
            TaskSetupAction.ToggleEndDate -> _state.update { it.copy(hasEndDate = !it.hasEndDate) }
            is TaskSetupAction.SetEndDate -> { /* TODO */ }
            is TaskSetupAction.AddSubtask -> addSubtask(action.text)
            is TaskSetupAction.RemoveSubtask -> removeSubtask(action.index)
            is TaskSetupAction.ToggleSubtask -> toggleSubtask(action.index)
            TaskSetupAction.OnSaveTask -> saveTask()
            TaskSetupAction.OnNavigateBack -> Timber.d("Navigate back")
        }
    }

    private fun updateTitle(text: String) {
        _state.update { it.copy(title = text) }
    }

    private fun updateDescription(text: String) {
        _state.update { it.copy(description = text) }
    }

    private fun setRecurrence(type: RecurrenceType) {
        _state.update { it.copy(recurrenceType = type) }
    }

    private fun toggleDay(dayIndex: Int) {
        _state.update { current ->
            val newDays =
                if (current.daysOfWeek.contains(dayIndex)) {
                    current.daysOfWeek - dayIndex
                } else {
                    current.daysOfWeek + dayIndex
                }
            current.copy(daysOfWeek = newDays)
        }
    }

    private fun addSubtask(text: String) {
        _state.update { current ->
            if (current.subtasks.size < 3) {
                current.copy(subtasks = current.subtasks + Subtask(title = text))
            } else {
                current
            }
        }
    }

    private fun removeSubtask(index: Int) {
        _state.update { current ->
            current.copy(subtasks = current.subtasks.filterIndexed { i, _ -> i != index })
        }
    }

    private fun toggleSubtask(index: Int) {
        Timber.d("Toggle subtask at $index")
    }

    private fun saveTask() {
        Timber.d("Saving task: ${_state.value}")
        viewModelScope.launch { _effect.emit(TaskItemEffect.SaveSuccess) }
    }
}
