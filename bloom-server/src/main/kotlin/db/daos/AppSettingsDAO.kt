package com.example.db.daos

import com.example.db.tables.AppSettingsTable
import com.example.model.AppSettingsDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class AppSettingsDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AppSettingsDAO>(AppSettingsTable)

    var userId by AppSettingsTable.userId
    var theme by AppSettingsTable.theme
    var weeklyGoal by AppSettingsTable.weeklyGoal
    var streakTarget by AppSettingsTable.streakTarget
    var emailEnabled by AppSettingsTable.emailEnabled
    var pushEnabled by AppSettingsTable.pushEnabled
    var habitRemindersEnabled by AppSettingsTable.habitRemindersEnabled
    var taskRemindersEnabled by AppSettingsTable.taskRemindersEnabled
    var updatedAt by AppSettingsTable.updatedAt
}

fun AppSettingsDAO.updateFrom(dto: AppSettingsDto) {
    theme = dto.theme
    weeklyGoal = dto.weeklyGoal
    streakTarget = dto.streakTarget
    emailEnabled = dto.emailEnabled
    pushEnabled = dto.pushEnabled
    habitRemindersEnabled = dto.habitRemindersEnabled
    taskRemindersEnabled = dto.taskRemindersEnabled
    updatedAt = dto.updatedAt
}
