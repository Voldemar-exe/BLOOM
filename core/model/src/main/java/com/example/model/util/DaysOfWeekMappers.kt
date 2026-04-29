package com.example.model.util

import java.time.DateTimeException
import java.time.YearMonth

fun mapDailyToMonth(): Set<Int> = (1..31).toSet()

fun weekToMonthDays(
    weekDays: Set<Int>,
    month: YearMonth,
): Set<Int> {
    val result = mutableSetOf<Int>()

    for (day in 1..month.lengthOfMonth()) {
        val date = month.atDay(day)
        val weekIndex = (date.dayOfWeek.value - 1)

        if (weekIndex in weekDays) {
            result += day
        }
    }

    return result
}

fun monthToWeekDays(
    monthDays: Set<Int>,
    month: YearMonth,
): Set<Int> {
    val result = mutableSetOf<Int>()

    for (day in monthDays) {
        try {
            val date = month.atDay(day)
            result += (date.dayOfWeek.value - 1)
        } catch (e: DateTimeException) {
        }
    }

    return result
}
