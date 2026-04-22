package com.example.model

data class AppConfig(
    val emailEnabled: Boolean,
    val pushEnabled: Boolean,
    val habitReminders: Boolean,
    val theme: String,
)
