package com.example.db.daos

import com.example.db.tables.SubtasksTable
import com.example.db.tables.TasksTable
import com.example.db.tables.UsersTable
import com.example.model.SubtaskDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class SubtaskDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SubtaskDAO>(SubtasksTable)

    var userId by SubtasksTable.userId
    var taskId by SubtasksTable.taskId
    var title by SubtasksTable.title
    var isChecked by SubtasksTable.isChecked
    var createdAt by SubtasksTable.createdAt
    var updatedAt by SubtasksTable.updatedAt
}

fun SubtaskDAO.Companion.create(
    userId: Long,
    taskId: Long,
    dto: SubtaskDto,
): SubtaskDAO =
    SubtaskDAO.new(dto.id.takeIf { it > 0 }) {
        this.userId = EntityID(userId, UsersTable)
        this.taskId = EntityID(taskId, TasksTable)
        updateFrom(dto)
        createdAt = dto.createdAt
    }

fun SubtaskDAO.updateFrom(dto: SubtaskDto) {
    title = dto.title
    isChecked = dto.isChecked
    updatedAt = dto.updatedAt
}
