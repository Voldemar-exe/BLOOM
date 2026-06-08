package com.example.habit.embedded.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.HabitRepository
import com.example.habit.usecases.UpdateHabitCreationUseCase
import com.example.model.Habit
import com.example.model.HabitPlant
import com.example.model.RecurrenceType
import com.example.model.Reminder
import com.example.model.Tag
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

@KoinViewModel
class HabitSetupViewModel(
    private val habitRepository: HabitRepository,
    private val updateHabitCreationUseCase: UpdateHabitCreationUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(HabitSetupState())
    val state: StateFlow<HabitSetupState> = _state.asStateFlow()

    private val _effect =
        MutableSharedFlow<HabitSetupEffect>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.SUSPEND,
        )
    val effect: SharedFlow<HabitSetupEffect> = _effect

    init {
        Timber.d("Start with ${_state.value}")
    }

    fun onAction(action: HabitSetupAction) {
        Timber.d("$action")
        when (action) {
            is HabitSetupAction.OnTitleChange -> _state.update { it.copy(title = action.text) }
            is HabitSetupAction.OnDescriptionChange ->
                _state.update {
                    it.copy(
                        description = action.text,
                    )
                }

            is HabitSetupAction.OnTagClick -> toggleTag(action.tag)
            is HabitSetupAction.SetRecurrenceType -> setRecurrence(action.type)
            is HabitSetupAction.UpdateSelectedDays -> updateSelectedDays(action.days)
            is HabitSetupAction.AddReminder -> addReminder(action.time)
            is HabitSetupAction.UpdateReminder -> updateReminder(action.index, action.time)
            is HabitSetupAction.ToggleReminder -> toggleReminder(action.index)
            is HabitSetupAction.RemoveReminder -> removeReminder(action.index)
            is HabitSetupAction.AddStep -> addStep(action.title)
            is HabitSetupAction.RemoveStep -> removeStep(action.index)
            is HabitSetupAction.ToggleStep -> toggleStep(action.index)
            is HabitSetupAction.SetPlant -> _state.update { it.copy(plant = action.plant) }
            HabitSetupAction.OnSaveHabit -> saveHabit()
            is HabitSetupAction.LoadHabit -> action.habitId?.let { loadHabit(it, action.plant) }
            HabitSetupAction.ToggleArchived -> _state.update { it.copy(isArchived = !it.isArchived) }
            HabitSetupAction.ToggleMuted -> _state.update { it.copy(isMuted = !it.isMuted) }
        }
    }

    private fun toggleTag(tag: Tag) {
        val current = _state.value.tags
        _state.update { it.copy(tags = if (tag in current) current - tag else current + tag) }
    }

    private fun setRecurrence(type: RecurrenceType) {
        _state.update { it.copy(recurrence = it.recurrence.copy(type = type)) }
        updateSelectedDays(emptySet())
    }

    private fun updateSelectedDays(days: Set<Int>) {
        _state.update { it.copy(recurrence = it.recurrence.copy(values = days)) }
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
                    current.reminders.mapIndexed { i, r ->
                        if (i == index) {
                            r.copy(time = LocalTime.of(time.first, time.second))
                        } else {
                            r
                        }
                    },
            )
        }
    }

    private fun toggleReminder(index: Int) {
        _state.update { current ->
            current.copy(
                reminders =
                    current.reminders.mapIndexed { i, r ->
                        if (i == index) r.copy(isEnabled = !r.isEnabled) else r
                    },
            )
        }
    }

    private fun removeReminder(index: Int) {
        _state.update { current ->
            val target = current.reminders.getOrNull(index) ?: return@update current
            val pending =
                if (target.id >
                    0L
                ) {
                    current.remindersToDelete + target.id
                } else {
                    current.remindersToDelete
                }
            current.copy(
                reminders = current.reminders.filterIndexed { i, _ -> i != index },
                remindersToDelete = pending,
            )
        }
    }

    private fun addStep(title: String) {
        val trimmed = title.trim()
        if (trimmed.isBlank()) return
        _state.update { current ->
            if (current.steps.size >= 3) {
                current
            } else {
                current.copy(steps = current.steps + trimmed)
            }
        }
    }

    private fun removeStep(index: Int) {
        _state.update { current ->
            if (index !in current.steps.indices) return@update current
            val newSteps = current.steps.filterIndexed { i, _ -> i != index }
            val newChecked =
                current.checkedSteps
                    .filter { it != index }
                    .map { if (it > index) it - 1 else it }
                    .toSet()
            current.copy(steps = newSteps, checkedSteps = newChecked)
        }
    }

    private fun toggleStep(index: Int) {
        _state.update { current ->
            if (index !in current.steps.indices) return@update current
            val set = current.checkedSteps
            current.copy(checkedSteps = if (index in set) set - index else set + index)
        }
    }

    private fun saveHabit() {
        viewModelScope.launch {
            // TODO: Add snackbar effect
            if (
                _state.value.recurrence.type != RecurrenceType.DAY &&
                _state.value.recurrence.values
                    .isEmpty()
            ) {
                return@launch
            }
            if (_state.value.id == 0L) {
                updateHabitCreationUseCase.invoke()
            }
            val habitId = habitRepository.saveHabit(stateToHabit(), _state.value.plant)

            _state.value.reminders.forEach {
                habitRepository.saveReminder(
                    it.copy(parentId = habitId),
                )
            }
            habitRepository.deleteRemindersByIds(_state.value.remindersToDelete.toList())

            _effect.emit(HabitSetupEffect.SaveSuccess)
        }
    }

    private fun loadHabit(
        habitId: Long,
        plant: HabitPlant?,
    ) {
        viewModelScope.launch {
            habitRepository.getHabitWithRelations(habitId)?.let { info ->
                _state.update {
                    it.copy(
                        id = info.habit.id,
                        title = info.habit.title,
                        description = info.habit.description,
                        recurrence = info.habit.recurrence,
                        tags = info.habit.tags,
                        steps = info.habit.steps,
                        reminders = info.reminders,
                        plant = plant ?: info.plant,
                        isArchived = info.habit.isArchived,
                        isPaused = info.habit.isPaused,
                        isMuted = info.habit.isMuted,
                        isChecked = info.habit.isChecked,
                    )
                }
            }
        }
    }

    private fun stateToHabit(): Habit {
        val current = _state.value
        Timber.i(current.toString())
        return Habit(
            id = current.id,
            title = current.title,
            description = current.description,
            recurrence = current.recurrence,
            tags = current.tags,
            steps = current.steps,
            isChecked = current.isChecked,
            isArchived = current.isArchived,
            isPaused = current.isPaused,
            isMuted = current.isMuted,
        )
    }
}
