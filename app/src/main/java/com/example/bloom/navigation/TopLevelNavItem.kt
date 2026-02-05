package com.example.bloom.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.habit.navigation.HabitNavKey
import com.example.profile.navigation.ProfileNavKey
import com.example.stats.navigation.StatsNavKey
import com.example.task.navigation.TaskNavKey


data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    /*@StringRes*/ val iconTextId: String,
    /*@StringRes*/ val titleTextId: String,
)

// TODO: Replace icons, replace with real StringRes

val HABIT = TopLevelNavItem(
    selectedIcon = Icons.Rounded.Home,
    unselectedIcon = Icons.Outlined.Home,
    iconTextId = "",
    titleTextId = "Привычки",
)

val TASKS = TopLevelNavItem(
    selectedIcon = Icons.Rounded.Menu,
    unselectedIcon = Icons.Outlined.Menu,
    iconTextId = "",
    titleTextId = "Задачи"
)

val STATS = TopLevelNavItem(
    selectedIcon = Icons.Rounded.Info,
    unselectedIcon = Icons.Outlined.Info,
    iconTextId = "",
    titleTextId = "Статистика"
)

val PROFILE = TopLevelNavItem(
    selectedIcon = Icons.Rounded.Person,
    unselectedIcon = Icons.Outlined.Person,
    iconTextId = "",
    titleTextId = "Профиль"
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HabitNavKey to HABIT,
    TaskNavKey to TASKS,
    StatsNavKey to STATS,
    ProfileNavKey to PROFILE
)