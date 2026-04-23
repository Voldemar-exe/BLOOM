package com.example.task.embedded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.TaskRepository
import com.example.model.RecurrenceType
import com.example.model.Subtask
import com.example.model.Task
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
    val taskRepository: TaskRepository,
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
            is TaskSetupAction.SetReminderTime -> { // TODO
            }

            TaskSetupAction.ToggleEndDate -> _state.update { it.copy(hasEndDate = !it.hasEndDate) }
            is TaskSetupAction.SetEndDate -> { // TODO
            }

            is TaskSetupAction.AddSubtask -> addSubtask(action.title)
            is TaskSetupAction.RemoveSubtask -> removeSubtask(action.index)
            is TaskSetupAction.ToggleSubtask -> toggleSubtask(action.index)
            TaskSetupAction.OnSaveTask -> saveTask()
            TaskSetupAction.OnNavigateBack -> Timber.d("Navigate back")
            is TaskSetupAction.LoadTask -> {
                action.taskId?.let { loadTask(it) }
            }
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
                current.copy(
                    subtasks =
                        current.subtasks +
                            Subtask(
                                taskId = _state.value.id,
                                title = text,
                            ),
                )
            } else {
                current
            }
        }
    }

    private fun removeSubtask(index: Int) {
        _state.update { current ->
            val target = current.subtasks.getOrNull(index) ?: return@update current

            val newPending =
                if (target.id > 0L) {
                    current.subtasksToDelete + target.id
                } else {
                    current.subtasksToDelete
                }

            current.copy(
                subtasks = current.subtasks.filterIndexed { i, _ -> i != index },
                subtasksToDelete = newPending,
            )
        }
    }

    private fun toggleSubtask(index: Int) {
        _state.update { current ->
            current.copy(
                subtasks =
                    current.subtasks.mapIndexed { i, subtask ->
                        if (i == index) subtask.copy(isChecked = !subtask.isChecked) else subtask
                    },
            )
        }
    }

    private fun saveTask() {
        Timber.d("Saving task: ${_state.value}")
        viewModelScope.launch {
            val taskId = taskRepository.saveTask(stateToTask())
            _state.value.reminders.forEach {
                taskRepository.saveReminder(it.copy(parentId = taskId))
            }
            _state.value.subtasks.forEach {
                taskRepository.saveSubtask(it.copy(taskId = taskId))
            }
            taskRepository.deleteSubtasksByIds(state.value.subtasksToDelete.toList())
            _effect.emit(TaskItemEffect.SaveSuccess)
        }
    }

    private fun loadTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.getTaskWithRelations(taskId)?.let { taskWithRelations ->
                _state.update {
                    it.copy(
                        id = taskWithRelations.task.id,
                        title = taskWithRelations.task.title,
                        description = taskWithRelations.task.description,
                        priority = taskWithRelations.task.priority,
                        daysOfWeek = taskWithRelations.task.daysOfWeek.toSet(),
                        deadline = taskWithRelations.task.deadline,
                        reminders = taskWithRelations.reminders,
                        tags = taskWithRelations.task.tags,
                        isArchived = taskWithRelations.task.isArchived,
                        isPaused = taskWithRelations.task.isPaused,
                        isMuted = taskWithRelations.task.isMuted,
                        subtasks = taskWithRelations.subtask,
                    )
                }
            }
        }
    }

    private fun stateToTask(): Task {
        val currState = _state.value

        return Task(
            id = currState.id,
            title = currState.title,
            description = currState.description,
            daysOfWeek = currState.daysOfWeek.toList(),
            priority = currState.priority,
            deadline = currState.deadline,
            tags = currState.tags,
            isChecked = currState.isChecked,
            isArchived = currState.isArchived,
            isPaused = currState.isPaused,
            isMuted = currState.isMuted,
        )
    }
}
