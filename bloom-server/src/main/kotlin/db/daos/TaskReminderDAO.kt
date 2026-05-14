package com.example.db.daos

import com.example.db.tables.TaskRemindersTable
import com.example.db.tables.TasksTable
import com.example.model.TaskReminderDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class TaskReminderDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskReminderDAO>(TaskRemindersTable)

    var taskId by TaskRemindersTable.taskId
    var reminderTime by TaskRemindersTable.reminderTime
    var isEnabled by TaskRemindersTable.isEnabled
    var createdAt by TaskRemindersTable.createdAt
    var updatedAt by TaskRemindersTable.updatedAt
}

fun TaskReminderDAO.Companion.create(
    taskId: Long,
    dto: TaskReminderDto,
): TaskReminderDAO =
    TaskReminderDAO.new(dto.id.takeIf { it > 0 }) {
        this.taskId = EntityID(taskId, TasksTable)
        updateFrom(dto)
        createdAt = dto.createdAt
    }

fun TaskReminderDAO.updateFrom(dto: TaskReminderDto) {
    reminderTime = dto.reminderTime
    isEnabled = dto.isEnabled
    updatedAt = dto.updatedAt
}
