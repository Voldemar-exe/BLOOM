package com.example.notification.util

import com.example.model.ReminderSchedule
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId

private const val MAX_TRIGGER_SEARCH_DAYS = 31L

fun ReminderSchedule.nextTriggerMillis(): Long? {
    val zone = ZoneId.systemDefault()
    val now = LocalDateTime.now().withSecond(0).withNano(0)

    for (offset in 0..MAX_TRIGGER_SEARCH_DAYS) {
        val targetDate = now.toLocalDate().plusDays(offset)

        if (targetDate.dayOfMonth !in daysOfMonth) continue

        val scheduled = LocalDateTime.of(targetDate, time).withNano(0)

        if (scheduled > now) {
            Timber.d("Next trigger found: %s", scheduled)
            return scheduled
                .atZone(zone)
                .toInstant()
                .toEpochMilli()
        }
    }

    return null
}
