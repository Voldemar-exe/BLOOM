package com.example.db.daos

import com.example.db.tables.HabitsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class HabitDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<HabitDAO>(HabitsTable)

    var userId by HabitsTable.userId
    var title by HabitsTable.title
    var description by HabitsTable.description
    var recurrence by HabitsTable.recurrence
    var tags by HabitsTable.tags
    var steps by HabitsTable.steps
    var isChecked by HabitsTable.isChecked
    var isArchived by HabitsTable.isArchived
    var isPaused by HabitsTable.isPaused
    var isMuted by HabitsTable.isMuted
    var startAt by HabitsTable.startAt
    var endAt by HabitsTable.endAt
    var createdAt by HabitsTable.createdAt
    var updatedAt by HabitsTable.updatedAt
    var syncStatus by HabitsTable.syncStatus
}
