package com.example.habit.embedded.plant

import com.example.model.HabitPlant

sealed interface PlantSetupEffect {
    data class Saved(val plant: HabitPlant) : PlantSetupEffect
}