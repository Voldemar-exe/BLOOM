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

            is PlantSetupAction.SetPetalColor -> {
                updatePlant { it.copy(petalColor = action.color) }
            }

            is PlantSetupAction.SetBaseColor -> {
                updatePlant { it.copy(baseColor = action.color) }
            }

            PlantSetupAction.Save -> save()
        }
    }

    private fun load(plant: HabitPlant) {
        _state.update { current ->
            current.copy(
                plant = plant,
                realPlant = plant,
            )
        }
    }

    private fun updatePlant(update: (HabitPlant) -> HabitPlant) {
        _state.update {
            it.copy(
                plant = update(it.plant).normalize(),
            )
        }
    }

    private fun undo() {
        _state.update { it.copy(plant = _state.value.realPlant.copy()) }
    }

    private fun save() {
        viewModelScope.launch {
            _effect.emit(PlantSetupEffect.Saved(_state.value.plant))
        }
    }

    private fun Float.round2(): Float = (kotlin.math.round(this * 100f) / 100f)

    private fun HabitPlant.normalize(): HabitPlant =
        copy(
            baseLength = baseLength.round2(),
            baseAngle = baseAngle.round2(),
            widthFalloff = widthFalloff.round2(),
            baseWidth = baseWidth.round2(),
            petalLength = petalLength.round2(),
        )
}
