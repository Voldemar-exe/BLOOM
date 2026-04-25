package com.example.habit.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.habit.embedded.item.HabitItemScreen
import com.example.habit.embedded.plant.HabitPlantScreen
import com.example.habit.home.HabitScreen
import com.example.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object HabitNavKey : NavKey

@Serializable
data class HabitItemNavKey(
    val habitId: Long?,
) : NavKey

@Serializable
data class HabitPlantNavKey(
    val habitId: Long?,
) : NavKey

fun EntryProviderScope<NavKey>.habitEntry(navigator: Navigator) {
    entry<HabitNavKey> {
        HabitScreen(
            onNavigate = {
                navigator.navigate(it)
            },
        )
    }
    entry<HabitItemNavKey> { navKey ->
        HabitItemScreen(
            habitId = navKey.habitId,
        )
    }
    entry<HabitPlantNavKey> { navKey ->
        HabitPlantScreen(
            habitId = navKey.habitId,
        )
    }
}
