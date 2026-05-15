package com.example.data.util

import com.example.database.dao.DailyLogAggregation
import com.example.database.model.StatsSourceType
import com.example.model.WeeklyActivityData
import com.example.model.WeeklyBySource
import java.time.LocalDate
import java.time.ZoneId

object StatsMapper {
    private const val DAYS_RANGE = 7

    fun mapToWeeklyActivity(rawLogs: List<DailyLogAggregation>): WeeklyActivityData {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()
        val startDate = today.plusDays((-DAYS_RANGE + 1).toLong())

        val habits = MutableList(DAYS_RANGE) { 0 }
        val tasks = MutableList(DAYS_RANGE) { 0 }
        val achievements = MutableList(DAYS_RANGE) { 0 }

        rawLogs.forEach { log ->
            val logDate =
                java.time.Instant
                    .ofEpochMilli(log.createdAt)
                    .atZone(zone)
                    .toLocalDate()
            if (logDate in startDate..today) {
                val dayIndex = startDate.daysUntil(logDate)
                when (log.sourceType) {
                    StatsSourceType.HABIT -> habits[dayIndex] += log.count
                    StatsSourceType.TASK -> tasks[dayIndex] += log.count
                    StatsSourceType.ACHIEVEMENT -> achievements[dayIndex] += log.count
                }
            }
        }

        val completions =
            habits.zip(tasks).zip(achievements).map { (ht, a) -> ht.first + ht.second + a }

        return WeeklyActivityData(
            completions = completions,
            bySource = WeeklyBySource(habits, tasks, achievements),
        )
    }
}

private fun LocalDate.daysUntil(other: LocalDate): Int {
    var count = 0
    var current = this
    while (current < other) {
        current = current.plusDays(1)
        count++
    }
    return count
}
