package com.example.db.daos

import com.example.db.tables.UserStatsTable
import com.example.model.UserStatsDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class UserStatsDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserStatsDAO>(UserStatsTable)

    var userId by UserStatsTable.userId
    var level by UserStatsTable.level
    var currentExperience by UserStatsTable.currentExperience
    var currentCoinsAmount by UserStatsTable.currentCoinsAmount
    var maxCoinsAmount by UserStatsTable.maxCoinsAmount
    var totalHabitsCreated by UserStatsTable.totalHabitsCreated
    var totalHabitsCompleted by UserStatsTable.totalHabitsCompleted
    var totalTasksCreated by UserStatsTable.totalTasksCreated
    var totalTasksCompleted by UserStatsTable.totalTasksCompleted
    var currentStreak by UserStatsTable.currentStreak
    var longestStreak by UserStatsTable.longestStreak
    var updatedAt by UserStatsTable.updatedAt
}

fun UserStatsDAO.updateFrom(dto: UserStatsDto) {
    level = dto.level
    currentExperience = dto.currentExperience
    currentCoinsAmount = dto.currentCoinsAmount
    maxCoinsAmount = dto.maxCoinsAmount
    totalHabitsCreated = dto.totalHabitsCreated
    totalHabitsCompleted = dto.totalHabitsCompleted
    totalTasksCreated = dto.totalTasksCreated
    totalTasksCompleted = dto.totalTasksCompleted
    currentStreak = dto.currentStreak
    longestStreak = dto.longestStreak
    updatedAt = dto.updatedAt
}