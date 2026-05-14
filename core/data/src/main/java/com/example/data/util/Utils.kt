package com.example.data.util

import com.example.model.Recurrence
import com.example.model.RecurrenceType
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime

fun stringToLocalTime(time: String): LocalTime {
    val pairTime = time.split(":").map { it.toInt() }
    return LocalTime.of(pairTime[0], pairTime[1])
}

fun Int.timeToTwoNumbers() = if (this.toString().length > 1) this.toString() else "0$this"

fun localTimeToString(time: LocalTime): String =
    "${time.hour.timeToTwoNumbers()}:${time.minute.timeToTwoNumbers()}"

fun Recurrence.occursInRange(
    start: LocalDate,
    end: LocalDate,
): Boolean {
    require(!end.isBefore(start)) {
        "End date must be after or equal to start date"
    }

    Timber.d("$start and $end")

    return generateSequence(start) { current ->
        current.plusDays(1).takeIf { !it.isAfter(end) }
    }.any { date ->
        when (type) {
            RecurrenceType.DAY -> true

            RecurrenceType.WEEK -> {
                require(values.isNotEmpty()) {
                    "Weekly recurrence requires at least one weekday"
                }

                Timber.d("${date.dayOfWeek.value - 1} || $values")
                (date.dayOfWeek.value - 1) in values
            }

            RecurrenceType.MONTH -> {
                require(values.isNotEmpty()) {
                    "Monthly recurrence requires at least one day"
                }

                (date.dayOfMonth - 1) in values
            }
        }
    }
}
