package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class HabitDto(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val recurrence: String,
    val tags: List<String>? = null,
    val steps: List<String>? = null,
    val isChecked: Boolean = false,
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val startAt: Long,
    val endAt: Long? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val plantDto: HabitPlantDto,
)

@Serializable
data class HabitPlantDto(
    val id: Long = 0,
    val habitId: Long,
    val presetId: Int,
    val iterations: Int,
    val variability: Float,
    val seed: Long,
    val baseAngle: Float,
    val baseLength: Float,
    val baseWidth: Float,
    val widthFalloff: Float,
    val widthFalloffEndAt: Float,
    val petalLength: Float,
    val petalType: String,
    val petalColor: Long,
    val baseColor: Long,
    val petalAlpha: Float,
    val createdAt: Long,
    val updatedAt: Long,
)

@Serializable
data class HabitReminderDto(
    val id: Long = 0,
    val habitId: Long,
    val reminderTime: String,
    val isEnabled: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long,
)

@Serializable
data class TaskDto(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val recurrence: String,
    val priority: String,
    val deadline: Long? = null,
    val tags: List<String>? = null,
    val isChecked: Boolean = false,
    val isArchived: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val subtasks: List<SubtaskDto> = listOf(),
)

@Serializable
data class SubtaskDto(
    val id: Long = 0,
    val taskId: Long,
    val title: String,
    val isChecked: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)

@Serializable
data class TaskReminderDto(
    val id: Long = 0,
    val taskId: Long,
    val reminderTime: String,
    val isEnabled: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long,
)

@Serializable
data class StatsLogDto(
    val id: Long = 0,
    val eventId: String,
    val sourceType: String,
    val sourceId: Long,
    val experienceDelta: Int,
    val coinsDelta: Int,
    val createdAt: Long,
)

@Serializable
data class HabitCompletionDto(
    val id: Long = 0,
    val habitId: Long,
    val completedAt: Long,
    val experienceEarned: Int,
    val coinsEarned: Int,
    val createdAt: Long,
)

@Serializable
data class TaskCompletionDto(
    val id: Long = 0,
    val taskId: Long,
    val completedAt: Long,
    val experienceEarned: Int,
    val coinsEarned: Int,
    val createdAt: Long,
)
