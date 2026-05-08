package com.example.db.daos

import com.example.db.tables.TaskRemindersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class TaskReminderDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskReminderDAO>(TaskRemindersTable)

    var userId by TaskRemindersTable.userId
    var taskId by TaskRemindersTable.taskId
    var reminderTime by TaskRemindersTable.reminderTime
    var isEnabled by TaskRemindersTable.isEnabled
    var createdAt by TaskRemindersTable.createdAt
    var updatedAt by TaskRemindersTable.updatedAt
}
