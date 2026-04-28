package com.example.model

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
                theme = "system",
                weeklyGoal = 5,
                streakTarget = 7,
                emailEnabled = true,
                pushEnabled = true,
                habitRemindersEnabled = true,
                taskRemindersEnabled = true,
            )
    }
}
