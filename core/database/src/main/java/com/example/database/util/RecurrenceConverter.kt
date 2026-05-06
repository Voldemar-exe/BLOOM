package com.example.database.util

import androidx.room.TypeConverter
import com.example.model.Recurrence
import com.example.model.RecurrenceType

class RecurrenceConverter {
    @TypeConverter
    fun fromPattern(recurrence: Recurrence): String =
        "${recurrence.type.name}:${recurrence.values.joinToString(",")}"

    @TypeConverter
    fun toPattern(value: String): Recurrence {
        val (typeStr, valuesStr) =
            value.split(":", limit = 2).let {
                it.first() to it.getOrElse(1) { "" }
            }
        val type = RecurrenceType.valueOf(typeStr)
        val values =
            valuesStr
                .takeIf { it.isNotBlank() }
                ?.split(",")
                ?.map(String::toInt)
                ?.toSet() ?: emptySet()
        return Recurrence(type, values)
    }
}
