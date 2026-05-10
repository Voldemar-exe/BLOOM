package com.example.db.daos

import com.example.db.tables.StatsLogsTable
import com.example.db.tables.UsersTable
import com.example.model.StatsLogDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class StatsLogDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<StatsLogDAO>(StatsLogsTable)

    var userId by StatsLogsTable.userId
    var eventId by StatsLogsTable.eventId
    var sourceType by StatsLogsTable.sourceType
    var sourceId by StatsLogsTable.sourceId
    var experienceDelta by StatsLogsTable.experienceDelta
    var coinsDelta by StatsLogsTable.coinsDelta
    var createdAt by StatsLogsTable.createdAt
}

fun StatsLogDAO.Companion.create(
    userId: Long,
    dto: StatsLogDto,
): StatsLogDAO =
    StatsLogDAO.new(dto.id.takeIf { it > 0 }) {
        this.userId = EntityID(userId, UsersTable)
        eventId = dto.eventId
        sourceType = dto.sourceType
        sourceId = dto.sourceId
        experienceDelta = dto.experienceDelta
        coinsDelta = dto.coinsDelta
        createdAt = dto.createdAt
    }
