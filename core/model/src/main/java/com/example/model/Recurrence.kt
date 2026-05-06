package com.example.model

data class Recurrence(val type: RecurrenceType, val values: Set<Int> = emptySet())
