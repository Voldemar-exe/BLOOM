package com.example.habit

import com.example.plant.GenerationConfig
import com.example.plant.PlantConfig

data class PlantState(
    val lSystemSentence: String,
    val id: Int,
    val label: String,
    val progress: Float = 0f,
    var isAnimating: Boolean = false,
    val plantConfig: PlantConfig,
    val generationConfig: GenerationConfig
)