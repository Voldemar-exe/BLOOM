package com.example.task.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.navigation.Navigator
import com.example.task.ui.TaskScreen
import kotlinx.serialization.Serializable

@Serializable
object TaskNavKey : NavKey

fun EntryProviderScope<NavKey>.taskEntry(navigator: Navigator) {
    entry<TaskNavKey> {
        TaskScreen()
    }
}