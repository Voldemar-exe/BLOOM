package com.example.db.daos

import com.example.db.tables.HabitRemindersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class HabitReminderDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<HabitReminderDAO>(HabitRemindersTable)

    var userId by HabitRemindersTable.userId
    var habitId by HabitRemindersTable.habitId
    var reminderTime by HabitRemindersTable.reminderTime
    var isEnabled by HabitRemindersTable.isEnabled
    var createdAt by HabitRemindersTable.createdAt
    var updatedAt by HabitRemindersTable.updatedAt
}
