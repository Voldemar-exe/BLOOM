package com.example.db.daos

import com.example.db.tables.TaskCompletionsTable
import com.example.db.tables.TasksTable
import com.example.model.TaskCompletionDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class TaskCompletionDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskCompletionDAO>(TaskCompletionsTable)

    var taskId by TaskCompletionsTable.taskId
    var completedAt by TaskCompletionsTable.completedAt
    var experienceEarned by TaskCompletionsTable.experienceEarned
    var coinsEarned by TaskCompletionsTable.coinsEarned
    var createdAt by TaskCompletionsTable.createdAt
}

fun TaskCompletionDAO.Companion.create(
    taskId: Long,
    dto: TaskCompletionDto,
): TaskCompletionDAO =
    TaskCompletionDAO.new(dto.id.takeIf { it > 0 }) {
        this.taskId = EntityID(taskId, TasksTable)
        completedAt = dto.completedAt
        experienceEarned = dto.experienceEarned
        coinsEarned = dto.coinsEarned
        createdAt = dto.createdAt
    }