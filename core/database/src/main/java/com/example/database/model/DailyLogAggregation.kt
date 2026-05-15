package com.example.database.model

data class DailyLogAggregation(
    val sourceType: StatsSourceType,
    val count: Int,
    val createdAt: Long,
)