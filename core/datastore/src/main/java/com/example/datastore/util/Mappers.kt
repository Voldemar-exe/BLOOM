package com.example.datastore.util

import com.example.model.AppSettings
import com.example.model.CustomizationItem
import com.example.model.CustomizationType
import com.example.model.User
import com.example.model.UserStats
import proto.AppSettingsProto
import proto.UserProto
import proto.UserStatsProto

fun UserProto.toDomain() =
    User(
        userId = userId,
        email = email,
        username = username,
        avatarKey = avatar,
        backgroundKey = background,
        colorKey = color,
        ownedAchievements = achievementsList.toSet(),
        ownedItems =
            purchasesList.map {
                CustomizationItem(
                    it.first,
                    CustomizationType.valueOf(it.second),
                )
            },
    )

fun User.toProto(): UserProto =
    UserProto
        .newBuilder()
        .setUserId(userId)
        .setEmail(email)
        .setUsername(username)
        .setAvatar(avatarKey)
        .setBackground(backgroundKey)
        .setColor(colorKey)
        .addAllAchievements(ownedAchievements)
        .addAllPurchases(
            ownedItems.map {
                UserProto.Purchase
                    .newBuilder()
                    .setFirst(it.key)
                    .setSecond(it.type.name)
                    .build()
            },
        ).build()

fun UserStatsProto.toDomain() =
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
        longestStreak = longestSteak,
    )

fun UserStats.toProto(): UserStatsProto =
    UserStatsProto
        .newBuilder()
        .setLevel(level)
        .setCurrentExperience(currentExperience)
        .setCurrentCoinsAmount(currentCoinsAmount)
        .setMaxCoinsAmount(maxCoinsAmount)
        .setTotalHabitsCreated(totalHabitsCreated)
        .setTotalHabitsCompleted(totalHabitsCompleted)
        .setTotalTasksCreated(totalTasksCreated)
        .setTotalTasksCompleted(totalTasksCompleted)
        .setCurrentStreak(currentStreak)
        .setLongestSteak(longestStreak)
        .build()

fun AppSettingsProto.toDomain() =
    AppSettings(
        theme = theme,
        weeklyGoal = weeklyGoal,
        streakTarget = streakTarget,
        emailEnabled = emailEnabled,
        pushEnabled = pushEnabled,
        habitRemindersEnabled = habitRemindersEnabled,
        taskRemindersEnabled = taskRemindersEnabled,
    )

fun AppSettings.toProto(): AppSettingsProto =
    AppSettingsProto
        .newBuilder()
        .setTheme(theme)
        .setWeeklyGoal(weeklyGoal)
        .setStreakTarget(streakTarget)
        .setEmailEnabled(emailEnabled)
        .setPushEnabled(pushEnabled)
        .setHabitRemindersEnabled(habitRemindersEnabled)
        .setTaskRemindersEnabled(taskRemindersEnabled)
        .build()
