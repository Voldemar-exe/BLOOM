package com.example.auth.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.auth.LoginScreen
import com.example.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object HabitNavKey : NavKey

fun EntryProviderScope<NavKey>.habitEntry(navigator: Navigator) {
    entry<HabitNavKey> {
        LoginScreen()
    }
}
