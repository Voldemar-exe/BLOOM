package com.example.plant

import androidx.compose.ui.graphics.Color


data class PlantConfig(
    val lSystemSentence: String,
    val leaves: LeafConfig,
    val branches: BranchConfig,
    val renderConfig: RenderConfig
)

data class GenerationConfig(
    val presetId: Int,
    val iterations: Int,
    val variability: Float,
    val seed: Long
)

data class LeafConfig(
    val length: Float,
    val type: LeafType
)
// TODO: After confirming all forms change names
enum class LeafType {
    TYPE0, TYPE1, TYPE2
}

data class BranchConfig(
    val baseLength: Float,
    val baseAngle: Float,
    val baseWidth: Float,
    val widthFalloff: Float,
    val minWidth: Float
)

// TODO: Thought about background colorizing
data class RenderConfig(
    val branchColor: Color,
    val leafColor: Color,
    val leafAlpha: Float
)
