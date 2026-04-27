package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

@Composable
fun rememberNavigationState(
    startKey: NavKey,
    topLevelKeys: Set<NavKey>,
): NavigationState {
    val topStack = rememberNavBackStack(startKey)
    val nestedStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }

    return remember(startKey, topLevelKeys) {
        NavigationState(
            startKey = startKey,
            topStack = topStack,
            subStacks = nestedStacks,
        )
    }
}

class NavigationState(
    val startKey: NavKey,
    val topStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    val currentTopLevelKey: NavKey by derivedStateOf { topStack.last() }

    val topKeys get() = subStacks.keys

    val currentSubStack: NavBackStack<NavKey>
        get() =
            subStacks[currentTopLevelKey]
                ?: error("Sub stack for $currentTopLevelKey does not exist")

    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }

    private val results = mutableMapOf<String, Any?>()

    fun <T> setResult(
        key: String,
        value: T?,
    ) {
        results[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> consumeResult(key: String): T? = results.remove(key) as? T
}

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries =
        subStacks.mapValues { (_, stack) ->
            val decorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
                    rememberViewModelStoreNavEntryDecorator<NavKey>(),
                )
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )
        }

    return topStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
