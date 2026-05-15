package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val login: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class RegisterRequest(
    val login: String,
    val email: String,
    val password: String,
)

@Serializable
data class SyncPushRequest(
    val habits: List<HabitDto> = emptyList(),
    val tasks: List<TaskDto> = emptyList(),
    val habitReminders: List<HabitReminderDto> = emptyList(),
    val taskReminders: List<TaskReminderDto> = emptyList(),
    val statsLogs: List<StatsLogDto> = emptyList(),
    val habitCompletions: List<HabitCompletionDto> = emptyList(),
    val taskCompletions: List<TaskCompletionDto> = emptyList(),
    val user: UserProfileDto? = null,
    val userStats: UserStatsDto? = null,
    val appSettings: AppSettingsDto? = null,
    val lastSyncTimestamp: Long = 0L,
)

@Serializable
data class SyncPullResponse(
    val habits: List<HabitDto> = emptyList(),
    val tasks: List<TaskDto> = emptyList(),
    val habitReminders: List<HabitReminderDto> = emptyList(),
    val taskReminders: List<TaskReminderDto> = emptyList(),
    val statsLogs: List<StatsLogDto> = emptyList(),
    val habitCompletions: List<HabitCompletionDto> = emptyList(),
    val taskCompletions: List<TaskCompletionDto> = emptyList(),
    val user: UserProfileDto? = null,
    val userStats: UserStatsDto? = null,
    val appSettings: AppSettingsDto? = null,
    val serverTimestamp: Long,
)
