package com.example.data.util

import com.example.database.model.StatsSourceType
import com.example.database.model.SyncStatus
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskCompletionEntity
import com.example.database.model.entities.TaskEntity
import com.example.database.model.entities.TaskReminderEntity
import com.example.database.util.RecurrenceConverter
import com.example.model.AppSettings
import com.example.model.CustomizationItem
import com.example.model.CustomizationType
import com.example.model.LeaderboardUser
import com.example.model.Priority
import com.example.model.User
import com.example.model.UserStats
import com.example.network.model.AppSettingsDto
import com.example.network.model.CustomizationItemDto
import com.example.network.model.HabitCompletionDto
import com.example.network.model.HabitDto
import com.example.network.model.HabitPlantDto
import com.example.network.model.HabitReminderDto
import com.example.network.model.LeaderboardUserDto
import com.example.network.model.StatsLogDto
import com.example.network.model.SubtaskDto
import com.example.network.model.TaskCompletionDto
import com.example.network.model.TaskDto
import com.example.network.model.TaskReminderDto
import com.example.network.model.UserProfileDto
import com.example.network.model.UserStatsDto

fun HabitEntity.toDto(plantEntity: HabitPlantEntity) =
    HabitDto(
        id = id,
        title = title,
        description = description,
        recurrence = RecurrenceConverter().fromRecurrence(recurrence),
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
        plantDto = plantEntity.toDto(),
    )

fun TaskEntity.toDto(subtasks: List<SubtaskEntity>) =
    TaskDto(
        id = id,
        title = title,
        description = description,
        recurrence = RecurrenceConverter().fromRecurrence(recurrence),
        priority = priority.name,
        deadline = deadline,
        tags = tags,
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
        createdAt = createdAt,
        updatedAt = updatedAt,
        subtasks = subtasks.map { it.toDto() },
    )

fun HabitReminderEntity.toDto() =
    HabitReminderDto(
        id = id,
        habitId = habitId,
        reminderTime = reminderTime,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun TaskReminderEntity.toDto() =
    TaskReminderDto(
        id = id,
        taskId = taskId,
        reminderTime = reminderTime,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun StatsLogEntity.toDto() =
    StatsLogDto(
        id = id,
        eventId = eventId,
        sourceType = sourceType.name,
        sourceId = sourceId,
        experienceDelta = experienceDelta,
        coinsDelta = coinsDelta,
        createdAt = createdAt,
    )

fun HabitCompletionEntity.toDto() =
    HabitCompletionDto(
        id = id,
        habitId = habitId,
        completedAt = completedAt,
        experienceEarned = experienceEarned,
        coinsEarned = coinsEarned,
        createdAt = createdAt,
    )

fun TaskCompletionEntity.toDto() =
    TaskCompletionDto(
        id = id,
        taskId = taskId,
        completedAt = completedAt,
        experienceEarned = experienceEarned,
        coinsEarned = coinsEarned,
        createdAt = createdAt,
    )

fun SubtaskEntity.toDto() =
    SubtaskDto(
        id = id,
        taskId = taskId,
        title = title,
        isChecked = isChecked,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun HabitPlantEntity.toDto() =
    HabitPlantDto(
        id = id,
        habitId = habitId,
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

fun HabitDto.toEntity() =
    HabitEntity(
        id = id,
        title = title,
        description = description ?: "",
        recurrence = RecurrenceConverter().toRecurrence(recurrence),
        tags = tags ?: emptyList(),
        steps = steps ?: emptyList(),
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
        startAt = startAt,
        endAt = endAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = SyncStatus.SYNCED,
    )

fun HabitPlantDto.toEntity() =
    HabitPlantEntity(
        id = id,
        habitId = habitId,
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

fun TaskDto.toEntity() =
    TaskEntity(
        id = id,
        title = title,
        description = description ?: "",
        recurrence = RecurrenceConverter().toRecurrence(recurrence),
        priority = Priority.valueOf(priority),
        deadline = deadline,
        tags = tags ?: emptyList(),
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = SyncStatus.SYNCED,
    )

fun SubtaskDto.toEntity() =
    SubtaskEntity(
        id = id,
        taskId = taskId,
        title = title,
        isChecked = isChecked,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun HabitReminderDto.toEntity() =
    HabitReminderEntity(
        id = id,
        habitId = habitId,
        reminderTime = reminderTime,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun TaskReminderDto.toEntity() =
    TaskReminderEntity(
        id = id,
        taskId = taskId,
        reminderTime = reminderTime,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun StatsLogDto.toEntity() =
    StatsLogEntity(
        id = id,
        eventId = eventId,
        sourceType = StatsSourceType.valueOf(sourceType),
        sourceId = sourceId,
        experienceDelta = experienceDelta,
        coinsDelta = coinsDelta,
        createdAt = createdAt,
    )

fun HabitCompletionDto.toEntity() =
    HabitCompletionEntity(
        id = id,
        habitId = habitId,
        completedAt = completedAt,
        experienceEarned = experienceEarned,
        coinsEarned = coinsEarned,
        createdAt = createdAt,
    )

fun TaskCompletionDto.toEntity() =
    TaskCompletionEntity(
        id = id,
        taskId = taskId,
        completedAt = completedAt,
        experienceEarned = experienceEarned,
        coinsEarned = coinsEarned,
        createdAt = createdAt,
    )

fun UserStats.toDto(lastSyncTimestamp: Long) =
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
        updatedAt = lastSyncTimestamp,
    )

fun UserStatsDto.toModel() =
    UserStats(
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
    )

fun AppSettings.toDto(lastSyncTimestamp: Long) =
    AppSettingsDto(
        theme = theme,
        weeklyGoal = weeklyGoal,
        streakTarget = streakTarget,
        emailEnabled = emailEnabled,
        pushEnabled = pushEnabled,
        habitRemindersEnabled = habitRemindersEnabled,
        taskRemindersEnabled = taskRemindersEnabled,
        updatedAt = lastSyncTimestamp,
    )

fun AppSettingsDto.toModel() =
    AppSettings(
        theme = theme,
        weeklyGoal = weeklyGoal,
        streakTarget = streakTarget,
        emailEnabled = emailEnabled,
        pushEnabled = pushEnabled,
        habitRemindersEnabled = habitRemindersEnabled,
        taskRemindersEnabled = taskRemindersEnabled,
    )

fun CustomizationItem.toDto() =
    CustomizationItemDto(
        key = key,
        type = type.name,
    )

fun CustomizationItemDto.toModel() =
    CustomizationItem(
        key = key,
        type = CustomizationType.valueOf(type),
    )

fun User.toDto(lastSyncTimestamp: Long) =
    UserProfileDto(
        id = userId,
        email = email,
        username = username,
        avatarKey = avatarKey,
        backgroundKey = backgroundKey,
        colorKey = colorKey,
        ownedAchievements = ownedAchievements,
        ownedItems = ownedItems.map { it.toDto() },
        updatedAt = lastSyncTimestamp,
    )

fun UserProfileDto.toModel() =
    User(
        userId = id,
        email = email,
        username = username,
        avatarKey = avatarKey,
        backgroundKey = backgroundKey,
        colorKey = colorKey,
        ownedAchievements = ownedAchievements,
        ownedItems = ownedItems.map { it.toModel() },
    )

fun LeaderboardUserDto.toModel() =
    LeaderboardUser(
        id = id,
        name = name,
        avatarKey = avatarKey,
        score = score,
    )
