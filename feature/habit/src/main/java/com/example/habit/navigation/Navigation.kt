package com.example.habit.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.habit.embedded.item.HabitItemScreen
import com.example.habit.embedded.plant.PlantSetupScreen
import com.example.habit.home.HabitScreen
import com.example.model.HabitPlant
import com.example.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object HabitNavKey : NavKey

@Serializable
data class HabitItemNavKey(val habitId: Long?, val progress: Float) : NavKey

@Serializable
data class HabitPlantNavKey(val initialPlant: HabitPlant) : NavKey

fun EntryProviderScope<NavKey>.habitEntry(navigator: Navigator) {
    entry<HabitNavKey> {
        HabitScreen(
            onOpenHabitSetup = { habitId, progress ->
                navigator.navigate(HabitItemNavKey(habitId, progress))
            },
        )
    }
    entry<HabitItemNavKey> { navKey ->
        val plant = navigator.consumeResult<HabitPlant>("plant_result")
        HabitItemScreen(
            habitId = navKey.habitId,
            progress = navKey.progress,
            plant = plant,
            onBack = { navigator.goBack() },
            onOpenPlantSetup = {
                navigator.navigate(HabitPlantNavKey(it))
            },
        )
    }
    entry<HabitPlantNavKey> { navKey ->
        PlantSetupScreen(
            initialPlant = navKey.initialPlant,
            onBack = { resultPlant ->
                resultPlant?.let {
                    navigator.setResult("plant_result", resultPlant)
                }
                navigator.goBack()
            },
        )
    }
}
