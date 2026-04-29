package com.example.task.embedded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.TaskRepository
import com.example.model.Priority
import com.example.model.RecurrenceType
import com.example.model.Reminder
import com.example.model.Subtask
import com.example.model.Tag
import com.example.model.Task
import com.example.model.util.weekToMonthDays
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
import java.time.LocalTime
import java.time.YearMonth

@KoinViewModel
class TaskSetupViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _state = MutableStateFlow(TaskSetupState())
    val state: StateFlow<TaskSetupState> = _state.asStateFlow()

    private val _effect =
        MutableSharedFlow<TaskSetupEffect>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.SUSPEND,
        )
    val effect: SharedFlow<TaskSetupEffect> = _effect

    fun onAction(action: TaskSetupAction) {
        Timber.d("$action")
        when (action) {
            is TaskSetupAction.OnTitleChange -> updateTitle(action.text)
            is TaskSetupAction.OnDescriptionChange -> updateDescription(action.text)
            is TaskSetupAction.OnPriorityClick -> setPriority(action.priority)
            is TaskSetupAction.OnTagClick -> handleTagSelect(action.tag)
            is TaskSetupAction.SetRecurrenceType -> setRecurrence(action.type)
            is TaskSetupAction.UpdateSelectedDays -> updateSelectedDays(action.days)
            is TaskSetupAction.AddReminder -> addReminder(action.time)
            is TaskSetupAction.UpdateReminder -> updateReminder(action.index, action.time)
            is TaskSetupAction.ToggleReminder -> toggleReminder(action.index)
            is TaskSetupAction.RemoveReminder -> removeReminder(action.index)
            TaskSetupAction.ToggleEndDate -> _state.update { it.copy(hasEndDate = !it.hasEndDate) }
            is TaskSetupAction.SetEndDate -> setEndDate(action.date)
            is TaskSetupAction.AddSubtask -> addSubtask(action.title)
            is TaskSetupAction.RemoveSubtask -> removeSubtask(action.index)
            is TaskSetupAction.ToggleSubtask -> toggleSubtask(action.index)
            TaskSetupAction.OnSaveTask -> saveTask()
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

    private fun setPriority(priority: Priority) {
        _state.update { it.copy(priority = priority) }
    }

    private fun handleTagSelect(tag: Tag) {
        val current = _state.value.tags
        _state.update { it.copy(tags = if (tag in current) current - tag else current + tag) }
    }

    private fun setRecurrence(type: RecurrenceType) {
        _state.update { it.copy(recurrenceType = type) }
    }

    private fun updateSelectedDays(days: Set<Int>) {
        val month = YearMonth.now()
        _state.update { current ->
            val normalizedDays =
                when (current.recurrenceType) {
                    RecurrenceType.DAY -> {
                        (1..31).toSet()
                    }

                    RecurrenceType.WEEK -> {
                        weekToMonthDays(days, month)
                    }

                    RecurrenceType.MONTH -> {
                        days
                    }
                }
            current.copy(daysOfWeek = normalizedDays)
        }
    }

    private fun addReminder(time: Pair<Int, Int>) {
        _state.update { current ->
            if (current.reminders.any {
                    it.time.hour == time.first && it.time.minute == time.second
                }
            ) {
                return@update current
            }

            current.copy(
                reminders =
                    current.reminders +
                        Reminder(
                            id = 0L,
                            parentId = current.id,
                            time = LocalTime.of(time.first, time.second),
                            isEnabled = true,
                        ),
            )
        }
    }

    private fun updateReminder(
        index: Int,
        time: Pair<Int, Int>,
    ) {
        _state.update { current ->
            current.copy(
                reminders =
                    current.reminders.mapIndexed { i, reminder ->
                        if (i == index) {
                            reminder.copy(
                                time =
                                    LocalTime.of(
                                        time.first,
                                        time.second,
                                    ),
                            )
                        } else {
                            reminder
                        }
                    },
            )
        }
    }

    private fun toggleReminder(index: Int) {
        _state.update { current ->
            current.copy(
                reminders =
                    current.reminders.mapIndexed { i, reminder ->
                        if (i == index) reminder.copy(isEnabled = !reminder.isEnabled) else reminder
                    },
            )
        }
    }

    private fun removeReminder(index: Int) {
        _state.update { current ->
            val target = current.reminders.getOrNull(index) ?: return@update current

            val newPending =
                if (target.id > 0L) {
                    current.remindersToDelete + target.id
                } else {
                    current.remindersToDelete
                }

            current.copy(
                reminders = current.reminders.filterIndexed { i, _ -> i != index },
                remindersToDelete = newPending,
            )
        }
    }

    private fun setEndDate(date: Long) {
        _state.update { it.copy(deadline = date) }
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
        viewModelScope.launch {
            val taskId = taskRepository.saveTask(stateToTask())
            _state.value.reminders.forEach {
                taskRepository.saveReminder(it.copy(parentId = taskId))
            }

            taskRepository.deleteRemindersByIds(
                _state.value.remindersToDelete.toList(),
            )

            _state.value.subtasks.forEach {
                taskRepository.saveSubtask(it.copy(taskId = taskId))
            }

            taskRepository.deleteSubtasksByIds(
                state.value.subtasksToDelete.toList(),
            )
            _effect.emit(TaskSetupEffect.SaveSuccess)
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
                        daysOfWeek = taskWithRelations.task.daysOfMonth.toSet(),
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
            daysOfMonth = currState.daysOfWeek.toList(),
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
