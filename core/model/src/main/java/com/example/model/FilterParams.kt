package com.example.model

data class FilterParams(
    val query: String,
    val tags: Set<Tag>,
    val tabTime: DayTimeInterval,
    val dateRange: DateRange,
)