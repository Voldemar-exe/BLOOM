package com.example.habit.models


data class PlantState(
    val commands: List<Command>,
    val label: String,
    val growthStages: List<List<Command>>,
    var currentStage: Int = 0,
    var progress: Float = 0f,
    var isAnimating: Boolean = false
)