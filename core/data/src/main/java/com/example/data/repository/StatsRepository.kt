package com.example.data.repository

import com.example.data.util.StatsMapper
import com.example.database.dao.StatsDao
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.model.UserStats
import com.example.model.WeeklyActivityData
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun getUserStats(): Flow<UserStats>

    suspend fun getWeeklyActivity(): WeeklyActivityData
}

class StatsRepositoryImpl(
    private val statsDao: StatsDao,
    private val dataStore: BloomPreferencesDataStore,
) : StatsRepository {
    override fun getUserStats(): Flow<UserStats> = dataStore.stats

    override suspend fun getWeeklyActivity(): WeeklyActivityData {
        val sinceTimestamp = System.currentTimeMillis() - (6L * 86400000)
        val rawLogs = statsDao.getAggregatedLogs(sinceTimestamp)
        return StatsMapper.mapToWeeklyActivity(rawLogs)
    }
}
