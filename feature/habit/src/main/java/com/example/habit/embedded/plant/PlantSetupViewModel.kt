package com.example.habit.embedded.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.HabitPlant
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
class PlantSetupViewModel : ViewModel() {
    private val _state = MutableStateFlow(PlantSetupState())
    val state: StateFlow<PlantSetupState> = _state.asStateFlow()

    private val _effect =
        MutableSharedFlow<PlantSetupEffect>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.SUSPEND,
        )
    val effect: SharedFlow<PlantSetupEffect> = _effect

    private val plantHistory = ArrayDeque<HabitPlant>()

    fun onAction(action: PlantSetupAction) {
        Timber.d("$action")
        when (action) {
            is PlantSetupAction.Load -> load(action.plant)

            PlantSetupAction.Undo -> undo()

            is PlantSetupAction.SelectPetal ->
                updatePlant {
                    it.copy(
                        petalType = action.petalType,
                    )
                }

            is PlantSetupAction.SelectPlantPreset ->
                updatePlant {
                    it.copy(
                        presetId = action.presetId,
                    )
                }

            is PlantSetupAction.SetBranchLength ->
                updatePlant {
                    it.copy(
                        baseLength = action.length,
                    )
                }

            is PlantSetupAction.SetBranchAngle ->
                updatePlant {
                    it.copy(
                        baseAngle = action.angle,
                    )
                }

            is PlantSetupAction.SetWidthFalloff ->
                updatePlant {
                    it.copy(
                        widthFalloff = action.widthFalloff,
                    )
                }

            is PlantSetupAction.SetBranchWidth ->
                updatePlant {
                    it.copy(
                        baseWidth = action.value,
                    )
                }

            is PlantSetupAction.SetMinWidth -> {}

            is PlantSetupAction.SetMaxWidth -> {}

            is PlantSetupAction.SetPetalLength ->
                updatePlant {
                    it.copy(
                        petalLength = action.value,
                    )
                }

            PlantSetupAction.Save -> save()
        }
    }

    private fun load(plant: HabitPlant) {
        _state.update { current ->
            current.copy(
                plant = plant,
            )
        }
    }

    private fun updatePlant(update: (HabitPlant) -> HabitPlant) {
        plantHistory.addLast(_state.value.plant)
        _state.update { it.copy(plant = update(it.plant)) }
        if (plantHistory.size > 2) plantHistory.removeFirst()
    }

    private fun undo() {
        val prev = plantHistory.removeLastOrNull() ?: return
        _state.update { it.copy(plant = prev) }
    }

    private fun save() {
        viewModelScope.launch {
            _effect.emit(PlantSetupEffect.Saved(_state.value.plant))
        }
    }
}
