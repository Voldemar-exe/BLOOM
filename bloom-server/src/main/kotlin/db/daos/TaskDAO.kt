package com.example.db.daos

import com.example.db.tables.TasksTable
import com.example.db.tables.UsersTable
import com.example.model.SyncStatus
import com.example.model.TaskDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class TaskDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskDAO>(TasksTable)

    var userId by TasksTable.userId
    var title by TasksTable.title
    var description by TasksTable.description
    var recurrence by TasksTable.recurrence
    var priority by TasksTable.priority
    var deadline by TasksTable.deadline
    var tags by TasksTable.tags
    var isChecked by TasksTable.isChecked
    var isArchived by TasksTable.isArchived
    var isPaused by TasksTable.isPaused
    var isMuted by TasksTable.isMuted
    var createdAt by TasksTable.createdAt
    var updatedAt by TasksTable.updatedAt
    var syncStatus by TasksTable.syncStatus
}

fun TaskDAO.Companion.create(
    userId: Long,
    dto: TaskDto,
): TaskDAO =
    TaskDAO.new(dto.id.takeIf { it > 0 }) {
        this.userId = EntityID(userId, UsersTable)
        updateFrom(dto)
        createdAt = dto.createdAt
        syncStatus = SyncStatus.SYNCED
    }

fun TaskDAO.updateFrom(dto: TaskDto) {
    title = dto.title
    description = dto.description
    recurrence = dto.recurrence
    priority = dto.priority
    deadline = dto.deadline
    tags = dto.tags
    isChecked = dto.isChecked
    isArchived = dto.isArchived
    isPaused = dto.isPaused
    isMuted = dto.isMuted
    updatedAt = dto.updatedAt
}
