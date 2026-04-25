package com.example.data.util

import java.time.LocalTime

fun stringToLocalTime(time: String): LocalTime {
    val pairTime = time.split(":").map { it.toInt() }
    return LocalTime.of(pairTime[0], pairTime[1])
}

fun Int.timeToTwoNumbers() = if (this.toString().length > 1) this.toString() else "0$this"

fun localTimeToString(time: LocalTime): String =
    "${time.hour.timeToTwoNumbers()}:${time.minute.timeToTwoNumbers()}"
