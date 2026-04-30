package com.example.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.database.model.StatsSourceType
import java.util.UUID

@Entity(tableName = "stats_logs")
data class StatsLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: String = UUID.randomUUID().toString(),
    val sourceType: StatsSourceType,
    val sourceId: Long,
    val experienceDelta: Int,
    val coinsDelta: Int,
    val createdAt: Long = System.currentTimeMillis(),
)
