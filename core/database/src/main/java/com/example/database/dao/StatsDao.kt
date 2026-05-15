package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.database.model.DailyLogAggregation

@Dao
interface StatsDao {
    @Query(
        """
        SELECT sourceType,
               COUNT(*) as count,
               createdAt
        FROM stats_logs
        WHERE createdAt > :sinceTimestamp
        GROUP BY sourceType, createdAt
    """,
    )
    suspend fun getAggregatedLogs(sinceTimestamp: Long): List<DailyLogAggregation>
}
