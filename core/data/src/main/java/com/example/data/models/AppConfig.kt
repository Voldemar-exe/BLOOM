package com.example.data.models

import android.provider.CalendarContract

data class AppConfig(
    val emailEnabled: Boolean,
    val pushEnabled: Boolean,
    val habitReminders: Boolean,
    val theme: String,
)
