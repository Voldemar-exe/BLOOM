package com.example.model

import java.time.LocalTime


data class Reminder(
    val id: Long,
    val parentId: Long,
    val time: LocalTime,
    val isEnabled: Boolean = false
)
