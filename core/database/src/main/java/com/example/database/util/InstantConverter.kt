package com.example.database.util

import androidx.room.TypeConverter
import kotlin.time.Instant

internal class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant): Long = instant.toEpochMilliseconds()

    @TypeConverter
    fun toInstant(timestamp: Long): Instant = Instant.fromEpochMilliseconds(timestamp)
}
