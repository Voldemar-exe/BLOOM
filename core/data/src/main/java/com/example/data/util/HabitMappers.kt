package com.example.data.util

import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.relationships.HabitWithPlantAndReminders
import com.example.model.Habit
import com.example.model.HabitPlant
import com.example.model.HabitWithRelations
import com.example.model.Reminder
import com.example.model.Tag

fun HabitEntity.asModel() =
    Habit(
        id = id,
        title = title,
        description = description,
        recurrence = recurrence,
        tags = tags.map { Tag.valueOf(it) }.toSet(),
        steps = steps,
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
    )

fun Habit.asEntity() =
    HabitEntity(
        id = id,
        title = title,
        description = description,
        recurrence = recurrence,
        tags = tags.map { it.name },
        steps = steps,
        isChecked = isChecked,
        isArchived = isArchived,
        isPaused = isPaused,
        isMuted = isMuted,
    )

fun HabitPlantEntity.asModel() =
    HabitPlant(
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
    )

fun HabitPlant.asEntity() =
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
    )

fun HabitReminderEntity.asModel() =
    Reminder(
        id = id,
        parentId = habitId,
        time = stringToLocalTime(reminderTime),
        isEnabled = isEnabled,
    )

fun Reminder.asHabitEntity() =
    HabitReminderEntity(
        id = id,
        habitId = parentId,
        reminderTime = localTimeToString(time),
        isEnabled = isEnabled,
    )

fun HabitWithPlantAndReminders.asModel(): HabitWithRelations =
    HabitWithRelations(
        habit = habit.asModel(),
        plant = plant.asModel(),
        reminders = habitReminders.map { it.asModel() },
    )