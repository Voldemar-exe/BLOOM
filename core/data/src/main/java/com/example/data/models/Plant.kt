package com.example.data.models

import java.util.UUID

data class Plant(
    val plantId: UUID,
    val habitId: UUID,
    val formula: String,
    val plantCommands: String,
    val isArchived: Boolean,
    // TODO Add Some how Log about growing
)
