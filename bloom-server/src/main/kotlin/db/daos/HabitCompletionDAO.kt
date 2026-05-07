package com.example.db.daos

import com.example.db.tables.HabitCompletionsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class HabitCompletionDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<HabitCompletionDAO>(HabitCompletionsTable)

    var userId by HabitCompletionsTable.userId
    var habitId by HabitCompletionsTable.habitId
    var completedAt by HabitCompletionsTable.completedAt
    var experienceEarned by HabitCompletionsTable.experienceEarned
    var coinsEarned by HabitCompletionsTable.coinsEarned
    var createdAt by HabitCompletionsTable.createdAt
}