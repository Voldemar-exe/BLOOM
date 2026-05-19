package com.example.db.daos

import com.example.db.tables.HabitPlantsTable
import com.example.model.HabitPlantDto
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class HabitPlantDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<HabitPlantDAO>(HabitPlantsTable)

    var habitId by HabitPlantsTable.habitId
    var presetId by HabitPlantsTable.presetId
    var iterations by HabitPlantsTable.iterations
    var variability by HabitPlantsTable.variability
    var seed by HabitPlantsTable.seed
    var baseAngle by HabitPlantsTable.baseAngle
    var baseLength by HabitPlantsTable.baseLength
    var baseWidth by HabitPlantsTable.baseWidth
    var widthFalloff by HabitPlantsTable.widthFalloff
    var widthFalloffEndAt by HabitPlantsTable.widthFalloffEndAt
    var petalLength by HabitPlantsTable.petalLength
    var petalType by HabitPlantsTable.petalType
    var petalColor by HabitPlantsTable.petalColor
    var baseColor by HabitPlantsTable.baseColor
    var petalAlpha by HabitPlantsTable.petalAlpha
    var createdAt by HabitPlantsTable.createdAt
    var updatedAt by HabitPlantsTable.updatedAt
}

fun HabitPlantDAO.Companion.findByIdAndHabit(
    id: Long,
    habitId: Long,
): HabitPlantDAO? =
    find { HabitPlantsTable.id eq id and (HabitPlantsTable.habitId eq habitId) }.firstOrNull()

fun HabitPlantDAO.Companion.create(
    habitId: EntityID<Long>,
    dto: HabitPlantDto,
): HabitPlantDAO =
    HabitPlantDAO.new {
        this.habitId = habitId
        updateFrom(dto)
        createdAt = dto.createdAt
    }

fun HabitPlantDAO.updateFrom(dto: HabitPlantDto) {
    presetId = dto.presetId
    iterations = dto.iterations
    variability = dto.variability
    seed = dto.seed
    baseAngle = dto.baseAngle
    baseLength = dto.baseLength
    baseWidth = dto.baseWidth
    widthFalloff = dto.widthFalloff
    widthFalloffEndAt = dto.widthFalloffEndAt
    petalLength = dto.petalLength
    petalType = dto.petalType
    petalColor = dto.petalColor
    baseColor = dto.baseColor
    petalAlpha = dto.petalAlpha
    updatedAt = dto.updatedAt
}
