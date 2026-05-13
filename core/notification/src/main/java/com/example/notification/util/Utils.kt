package com.example.notification.util

import com.example.model.RecurrenceType
import com.example.model.ReminderSchedule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun ReminderSchedule.nextTriggerMillis(): Long? {
    val zone = ZoneId.systemDefault()
    val now = LocalDateTime.now().withSecond(0).withNano(0)

    fun findNextDate(predicate: (LocalDate) -> Boolean): LocalDateTime? =
        generateSequence(now.toLocalDate()) { it.plusDays(1) }
            .firstOrNull(predicate)
            ?.takeIf { date ->
                LocalDateTime.of(date, time).isAfter(now)
            }?.let { LocalDateTime.of(it, time) }

    return when (recurrence.type) {
        RecurrenceType.DAY -> {
            val today = LocalDate.now()
            val candidate = LocalDateTime.of(today, time)
            (candidate.takeIf { it.isAfter(now) } ?: LocalDateTime.of(today.plusDays(1), time))
                .atZone(zone)
                .toInstant()
                .toEpochMilli()
        }

        RecurrenceType.WEEK -> {
            require(recurrence.values.isNotEmpty()) {
                "Weekly recurrence requires at least one weekday"
            }

            findNextDate { it.dayOfWeek.value - 1 in recurrence.values }
                ?.atZone(zone)
                ?.toInstant()
                ?.toEpochMilli()
        }

        RecurrenceType.MONTH -> {
            require(recurrence.values.isNotEmpty()) {
                "Monthly recurrence requires at least one day"
            }

            findNextDate { it.dayOfMonth - 1 in recurrence.values }
                ?.atZone(zone)
                ?.toInstant()
                ?.toEpochMilli()
        }
    }
}
