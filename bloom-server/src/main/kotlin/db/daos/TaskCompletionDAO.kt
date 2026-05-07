package com.example.db.daos

import com.example.db.tables.TaskCompletionsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class TaskCompletionDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskCompletionDAO>(TaskCompletionsTable)

    var userId by TaskCompletionsTable.userId
    var taskId by TaskCompletionsTable.taskId
    var completedAt by TaskCompletionsTable.completedAt
    var experienceEarned by TaskCompletionsTable.experienceEarned
    var coinsEarned by TaskCompletionsTable.coinsEarned
    var createdAt by TaskCompletionsTable.createdAt
}