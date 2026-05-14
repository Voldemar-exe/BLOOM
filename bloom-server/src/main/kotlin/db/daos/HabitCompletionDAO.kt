package com.example.db.daos

import com.example.db.tables.HabitCompletionsTable
import com.example.db.tables.HabitsTable
import com.example.model.HabitCompletionDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class HabitCompletionDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<HabitCompletionDAO>(HabitCompletionsTable)

    var habitId by HabitCompletionsTable.habitId
    var completedAt by HabitCompletionsTable.completedAt
    var experienceEarned by HabitCompletionsTable.experienceEarned
    var coinsEarned by HabitCompletionsTable.coinsEarned
    var createdAt by HabitCompletionsTable.createdAt
}

fun HabitCompletionDAO.Companion.create(
    habitId: Long,
    dto: HabitCompletionDto,
): HabitCompletionDAO =
    HabitCompletionDAO.new(dto.id.takeIf { it > 0 }) {
        this.habitId = EntityID(habitId, HabitsTable)
        completedAt = dto.completedAt
        experienceEarned = dto.experienceEarned
        coinsEarned = dto.coinsEarned
        createdAt = dto.createdAt
    }
