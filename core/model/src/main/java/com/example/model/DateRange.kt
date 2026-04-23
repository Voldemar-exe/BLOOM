package com.example.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class DateRange(
    val start: LocalDate? = null,
    val end: LocalDate? = null,
)

fun DateRange.onDateSelected(date: LocalDate): DateRange {
    return when {
        start == null -> copy(start = date)
        end == null -> {
            if (date < start) {
                copy(start = date, end = start)
            } else {
                copy(end = date)
            }
        }
        else -> DateRange(start = date, end = null)
    }
}