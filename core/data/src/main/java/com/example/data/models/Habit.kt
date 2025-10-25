package com.example.data.models

import java.util.UUID

data class Habit(
    val id: UUID,
    val name: String,
    val createdAt: Long,
    val expiredAt: Long
)
