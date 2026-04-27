package com.example.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class HabitPlant(
    val id: Long,
    val habitId: Long,
    val presetId: Int,
    val iterations: Int,
    val variability: Float,
    val seed: Long,
    val baseAngle: Float,
    val baseLength: Float,
    val baseWidth: Float,
    val widthFalloff: Float,
    val widthFalloffEndAt: Float,
    val petalLength: Float,
    val petalType: String,
    val petalColor: Long,
    val baseColor: Long,
    val petalAlpha: Float,
)
