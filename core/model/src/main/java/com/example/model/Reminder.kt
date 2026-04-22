package com.example.model

import kotlin.time.Instant


data class Reminder(
    val id: Long,
    val parentId: Long,
    val time: Instant,
    val isEnabled: Boolean = false
)
