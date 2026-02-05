package com.example.stats.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.navigation.Navigator
import com.example.stats.ui.StatsScreen
import kotlinx.serialization.Serializable

@Serializable
object StatsNavKey : NavKey

fun EntryProviderScope<NavKey>.statsEntry(navigator: Navigator) {
    entry<StatsNavKey> {
        StatsScreen()
    }
}