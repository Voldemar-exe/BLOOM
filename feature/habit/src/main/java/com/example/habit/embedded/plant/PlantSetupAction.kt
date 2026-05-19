package com.example.habit.embedded.plant

import com.example.model.HabitPlant

sealed interface PlantSetupAction {
    data class Load(val plant: HabitPlant) : PlantSetupAction

    object Undo : PlantSetupAction

    data class SelectPetal(val petalType: String) : PlantSetupAction

    data class SelectPlantPreset(val presetId: Int) : PlantSetupAction

    data class SetBranchLength(val length: Float) : PlantSetupAction

    data class SetBranchAngle(val angle: Float) : PlantSetupAction

    data class SetWidthFalloff(val widthFalloff: Float) : PlantSetupAction

    data class SetBranchWidth(val value: Float) : PlantSetupAction

    data class SetMinWidth(val value: Float) : PlantSetupAction

    data class SetMaxWidth(val value: Float) : PlantSetupAction

    data class SetPetalLength(val value: Float) : PlantSetupAction

    data class SetPetalColor(val color: Long) : PlantSetupAction

    data class SetBaseColor(val color: Long) : PlantSetupAction

    object Save : PlantSetupAction
}
