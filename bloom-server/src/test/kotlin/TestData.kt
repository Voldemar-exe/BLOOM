package com.example

import com.example.model.*

object TestData {
    private fun now() = System.currentTimeMillis()

    fun habitDto(
        id: Long = 1L,
        updatedAt: Long = now(),
    ) = HabitDto(
        id = id,
        title = "Drink water",
        description = "Daily habit",
        recurrence = "DAILY",
        tags = listOf("health"),
        steps = listOf("Step 1"),
        isChecked = false,
        isArchived = false,
        isPaused = false,
        isMuted = false,
        startAt = now(),
        endAt = null,
        createdAt = now(),
        updatedAt = updatedAt,
        plantDto = habitPlantDto(habitId = id),
    )

    fun habitPlantDto(
        id: Long = 1L,
        habitId: Long = 1L,
    ) = HabitPlantDto(
        id = id,
        habitId = habitId,
        presetId = 1,
        iterations = 3,
        variability = 0.5f,
        seed = 12345L,
        baseAngle = 45f,
        baseLength = 100f,
        baseWidth = 10f,
        widthFalloff = 0.8f,
        widthFalloffEndAt = 0.3f,
        petalLength = 15f,
        petalType = "ROUND",
        petalColor = 0xFF00FF,
        baseColor = 0x00FF00,
        petalAlpha = 1f,
        createdAt = now(),
        updatedAt = now(),
    )

    fun taskDto(
        id: Long = 1L,
        updatedAt: Long = now(),
    ) = TaskDto(
        id = id,
        title = "Finish diploma",
        description = "Write tests",
        recurrence = "NONE",
        priority = "HIGH",
        deadline = null,
        tags = listOf("study"),
        isChecked = false,
        isArchived = false,
        isPaused = false,
        isMuted = false,
        createdAt = now(),
        updatedAt = updatedAt,
        subtasks = listOf(subtaskDto(taskId = id)),
    )

    fun subtaskDto(
        id: Long = 1L,
        taskId: Long = 1L,
    ) = SubtaskDto(
        id = id,
        taskId = taskId,
        title = "Write sync tests",
        isChecked = false,
        createdAt = now(),
        updatedAt = now(),
    )

    fun statsLogDto(
        id: Long = 1L,
        eventId: String = "event_1",
    ) = StatsLogDto(
        id = id,
        eventId = eventId,
        sourceType = "TASK",
        sourceId = 1L,
        experienceDelta = 50,
        coinsDelta = 10,
        createdAt = now(),
    )
}
