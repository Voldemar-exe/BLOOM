package com.example.task.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.navigation.Navigator
import com.example.task.embedded.TaskItemScreen
import com.example.task.home.TaskScreen
import kotlinx.serialization.Serializable

@Serializable
object TaskNavKey : NavKey

@Serializable
data class TaskItemNavKey(
    val id: Long?,
) : NavKey

fun EntryProviderScope<NavKey>.taskEntry(navigator: Navigator) {
    entry<TaskNavKey> {
        TaskScreen(
            onNavigate = {
                navigator.navigate(it)
            },
        )
    }
    entry<TaskItemNavKey> { navKey ->
        TaskItemScreen(
            taskId = navKey.id,
            onBack = { navigator.goBack() },
        )
    }
}
