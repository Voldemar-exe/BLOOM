package com.example.habit.embedded.plant

import androidx.compose.runtime.Immutable
import com.example.model.HabitPlant

@Immutable
data class PlantSetupState(
    val plant: HabitPlant =
        HabitPlant(
            id = 0L,
            habitId = 0L,
            presetId = 0,
            iterations = 3,
            variability = 0.5f,
            seed = 1L,
            baseAngle = 90f,
            baseLength = 25f,
            baseWidth = 20f,
            widthFalloff = 1f,
            widthFalloffEndAt = 0.1f,
            petalLength = 25f,
            petalType = "TYPE1",
            petalColor = 0L,
            baseColor = 0L,
            petalAlpha = 1f,
        ),
    val realPlant: HabitPlant = plant.copy(),
)
