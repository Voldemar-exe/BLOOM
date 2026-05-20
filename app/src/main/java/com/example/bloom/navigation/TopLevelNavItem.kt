package com.example.bloom.navigation

import com.example.designsystem.picture.BloomIcons
import com.example.habit.navigation.HabitNavKey
import com.example.profile.navigation.ProfileNavKey
import com.example.stats.navigation.StatsNavKey
import com.example.task.navigation.TaskNavKey

data class TopLevelNavItem(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    // @StringRes
    val iconTextId: String,
    // @StringRes
    val titleTextId: String,
)

// TODO: replace with real StringRes

val HABIT =
    TopLevelNavItem(
        selectedIcon = BloomIcons.HabitsFill,
        unselectedIcon = BloomIcons.Habits,
        iconTextId = "",
        titleTextId = "Привычки",
    )

val TASKS =
    TopLevelNavItem(
        selectedIcon = BloomIcons.TasksFill,
        unselectedIcon = BloomIcons.Tasks,
        iconTextId = "",
        titleTextId = "Задачи",
    )

val STATS =
    TopLevelNavItem(
        selectedIcon = BloomIcons.StatsFill,
        unselectedIcon = BloomIcons.Stats,
        iconTextId = "",
        titleTextId = "Статистика",
    )

val PROFILE =
    TopLevelNavItem(
        selectedIcon = BloomIcons.ProfileFill,
        unselectedIcon = BloomIcons.Profile,
        iconTextId = "",
        titleTextId = "Профиль",
    )

val TOP_LEVEL_NAV_ITEMS =
    mapOf(
        HabitNavKey to HABIT,
        TaskNavKey to TASKS,
        StatsNavKey to STATS,
        ProfileNavKey to PROFILE,
    )
