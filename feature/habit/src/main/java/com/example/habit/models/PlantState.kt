package com.example.habit.models

import com.example.plant.PlantConfig

data class PlantState(
    val lSystemSentence: String,
    val label: String,
    val progress: Float = 0f,
    var isAnimating: Boolean = false,
    val plantConfig: PlantConfig
)