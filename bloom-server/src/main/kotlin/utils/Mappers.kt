package com.example.utils

import com.example.db.daos.*
import com.example.db.tables.HabitPlantsTable
import com.example.db.tables.SubtasksTable
import com.example.model.*
import org.jetbrains.exposed.v1.core.eq

fun HabitDAO.toDto() =
    HabitDto(
        id = id.value,
        title = title,
        description = description,
        recurrence = recurrence,
        tags = tags,
        steps = steps,
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
        startAt = startAt,
        endAt = endAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        plantDto =
            HabitPlantDAO.find { HabitPlantsTable.habitId eq id }.firstOrNull()?.toDto()
                ?: HabitPlantDto.defaultForHabit(id.value, createdAt, updatedAt),
    )

fun HabitPlantDAO.toDto() =
    HabitPlantDto(
        id = id.value,
        habitId = habitId.value,
        presetId = presetId,
        iterations = iterations,
        variability = variability,
        seed = seed,
        baseAngle = baseAngle,
        baseLength = baseLength,
        baseWidth = baseWidth,
        widthFalloff = widthFalloff,
        widthFalloffEndAt = widthFalloffEndAt,
        petalLength = petalLength,
        petalType = petalType,
        petalColor = petalColor,
        baseColor = baseColor,
        petalAlpha = petalAlpha,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun HabitPlantDto.Companion.defaultForHabit(
    habitId: Long,
    createdAt: Long,
    updatedAt: Long,
) = HabitPlantDto(
    habitId = habitId,
    presetId = 0,
    iterations = 0,
    variability = 0f,
    seed = 0,
    baseAngle = 0f,
    baseLength = 0f,
    baseWidth = 0f,
    widthFalloff = 0f,
    widthFalloffEndAt = 0f,
    petalLength = 0f,
    petalType = "",
    petalColor = 0,
    baseColor = 0,
    petalAlpha = 0f,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun TaskDAO.toDto() =
    TaskDto(
        id = id.value,
        title = title,
        description = description,
        recurrence = recurrence,
        priority = priority,
        deadline = deadline,
        tags = tags,
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
        createdAt = createdAt,
        updatedAt = updatedAt,
        subtasks = SubtaskDAO.find { SubtasksTable.taskId eq id }.map { it.toDto() },
    )

fun SubtaskDAO.toDto() =
    SubtaskDto(
        id = id.value,
        taskId = taskId.value,
        title = title,
        isChecked = isChecked,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun HabitReminderDAO.toDto() =
    HabitReminderDto(
        id = id.value,
        habitId = habitId.value,
        reminderTime = reminderTime,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun TaskReminderDAO.toDto() =
    TaskReminderDto(
        id = id.value,
        taskId = taskId.value,
        reminderTime = reminderTime,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun StatsLogDAO.toDto() =
    StatsLogDto(
        id = id.value,
        eventId = eventId,
        sourceType = sourceType,
        sourceId = sourceId,
        experienceDelta = experienceDelta,
        coinsDelta = coinsDelta,
        createdAt = createdAt,
    )

fun HabitCompletionDAO.toDto() =
    HabitCompletionDto(
        id = id.value,
        habitId = habitId.value,
        completedAt = completedAt,
        experienceEarned = experienceEarned,
        coinsEarned = coinsEarned,
        createdAt = createdAt,
    )

fun TaskCompletionDAO.toDto() =
    TaskCompletionDto(
        id = id.value,
        taskId = taskId.value,
        completedAt = completedAt,
        experienceEarned = experienceEarned,
        coinsEarned = coinsEarned,
        createdAt = createdAt,
    )

fun UserDAO.toProfileDto(
    achievements: Set<Int> = emptySet(),
    items: List<CustomizationItemDto> = emptyList(),
): UserProfileDto =
    UserProfileDto(
        id = id.value,
        email = email,
        username = nickname,
        avatarKey = avatar,
        backgroundKey = background,
        colorKey = color,
        ownedAchievements = achievements,
        ownedItems = items,
        updatedAt = updatedAt,
    )

fun UserStatsDAO.toDto(): UserStatsDto =
    UserStatsDto(
        level = level,
        currentExperience = currentExperience,
        currentCoinsAmount = currentCoinsAmount,
        maxCoinsAmount = maxCoinsAmount,
        totalHabitsCreated = totalHabitsCreated,
        totalHabitsCompleted = totalHabitsCompleted,
        totalTasksCreated = totalTasksCreated,
        totalTasksCompleted = totalTasksCompleted,
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        updatedAt = updatedAt,
    )

fun AppSettingsDAO.toDto(): AppSettingsDto =
    AppSettingsDto(
        theme = theme,
        weeklyGoal = weeklyGoal,
        streakTarget = streakTarget,
        emailEnabled = emailEnabled,
        pushEnabled = pushEnabled,
        habitRemindersEnabled = habitRemindersEnabled,
        taskRemindersEnabled = taskRemindersEnabled,
        updatedAt = updatedAt,
    )