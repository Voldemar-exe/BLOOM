package com.example.db.daos

import com.example.db.tables.SubtasksTable
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
