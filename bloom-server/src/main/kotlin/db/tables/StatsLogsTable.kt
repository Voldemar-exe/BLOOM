package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object StatsLogsTable : LongIdTable("stats_logs") {
    val userId = reference("user_id", UsersTable.id).index()
    val eventId = text("event_id").uniqueIndex()
    val sourceType = text("source_type")
    val sourceId = long("source_id")
    val experienceDelta = integer("experience_delta")
    val coinsDelta = integer("coins_delta")
    val createdAt = long("created_at")
}