package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "habit_plants")
data class HabitPlant(
    @PrimaryKey(autoGenerate = true)
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
    val petalColor: Long, // Store as RGBa
    val baseColor: Long, // Store as RGBa
    val petalAlpha: Float,
    val createdAt: Instant,
    val updatedAt: Instant
)