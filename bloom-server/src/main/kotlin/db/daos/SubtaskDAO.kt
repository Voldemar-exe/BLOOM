package com.example.db.daos

import com.example.db.tables.SubtasksTable
import com.example.model.SubtaskDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class SubtaskDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SubtaskDAO>(SubtasksTable)

    var taskId by SubtasksTable.taskId
    var title by SubtasksTable.title
    var isChecked by SubtasksTable.isChecked
    var createdAt by SubtasksTable.createdAt
    var updatedAt by SubtasksTable.updatedAt
}

fun SubtaskDAO.Companion.create(
    taskId: EntityID<Long>,
    dto: SubtaskDto,
): SubtaskDAO =
    SubtaskDAO.new {
        this.taskId = taskId
        updateFrom(dto)
        createdAt = dto.createdAt
    }

fun SubtaskDAO.updateFrom(dto: SubtaskDto) {
    title = dto.title
    isChecked = dto.isChecked
    updatedAt = dto.updatedAt
}
