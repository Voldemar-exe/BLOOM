package com.example.model

import androidx.compose.runtime.Immutable

@Immutable
data class AppSettings(
    val theme: String,
    val weeklyGoal: Int,
    val streakTarget: Int,
    val emailEnabled: Boolean,
    val pushEnabled: Boolean,
    val habitRemindersEnabled: Boolean,
    val taskRemindersEnabled: Boolean,
) {
    companion object {
        fun default() =
            AppSettings(
                theme = "SYSTEM",
                weeklyGoal = 5,
                streakTarget = 7,
                emailEnabled = true,
                pushEnabled = true,
                habitRemindersEnabled = true,
                taskRemindersEnabled = true,
            )
    }
}

data class AppearanceSettings(val theme: String)

data class PreferenceSettings(val weeklyGoal: Int, val streakTarget: Int)

data class NotificationSettings(val emailEnabled: Boolean, val pushEnabled: Boolean)

data class ReminderSettings(val habitRemindersEnabled: Boolean, val taskRemindersEnabled: Boolean)
