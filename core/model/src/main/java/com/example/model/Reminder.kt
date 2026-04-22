package com.example.model

import java.time.LocalTime

data class Reminder(
    val time: LocalTime = LocalTime.now(),
    val isOn: Boolean = false
)
