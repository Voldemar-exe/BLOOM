package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object HabitPlantsTable : LongIdTable("habit_plants") {
    val habitId = reference("habit_id", HabitsTable.id).index()
    val presetId = integer("preset_id")
    val iterations = integer("iterations")
    val variability = float("variability")
    val seed = long("seed")
    val baseAngle = float("base_angle")
    val baseLength = float("base_length")
    val baseWidth = float("base_width")
    val widthFalloff = float("width_falloff")
    val widthFalloffEndAt = float("width_falloff_end_at")
    val petalLength = float("petal_length")
    val petalType = text("petal_type")
    val petalColor = long("petal_color")
    val baseColor = long("base_color")
    val petalAlpha = float("petal_alpha")
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}
