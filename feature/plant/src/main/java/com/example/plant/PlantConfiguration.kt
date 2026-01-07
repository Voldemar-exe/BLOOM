package com.example.plant

import androidx.compose.ui.graphics.Color


data class PlantConfig(
    val lSystemSentence: String,
    val leaves: LeafConfig,
    val branches: BranchConfig,
    val theme: PlantTheme,
    val isAnimated: Boolean
)

data class GenerationConfig(
    val presetIndex: Int,
    val iterations: Int,
    val variability: Float,
    val seed: Long
)

data class LeafConfig(
    val type: LeafType,
    val length: Float,
    val width: Float
)
// TODO: After confirming all forms change names
enum class LeafType {
    TYPE0, TYPE1, TYPE2
}

data class BranchConfig(
    val length: Float,
    val angle: Float,
    val width: Float,
    val widthFalloff: Float,
    val minWidth: Float
)

// TODO: Thought about background colorizing
data class PlantTheme(
    val branchColor: Color,
    val leafColor: Color,
    val leafAlpha: Float
)
