package com.example.bloom.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.auth.navigation.authEntry
import com.example.bloom.navigation.TOP_LEVEL_NAV_ITEMS
import com.example.habit.navigation.HabitNavKey
import com.example.habit.navigation.habitEntry
import com.example.navigation.Navigator
import com.example.navigation.rememberNavigationState
import com.example.navigation.toEntries
import com.example.profile.navigation.profileEntry
import com.example.stats.navigation.statsEntry
import com.example.task.navigation.taskEntry

@Composable
fun BloomApp() {

    val navigationState = rememberNavigationState(HabitNavKey, TOP_LEVEL_NAV_ITEMS.keys)

    val navigator = remember { Navigator(navigationState) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
                    val selected = navKey == navigationState.currentTopLevelKey
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selected) navItem.selectedIcon else navItem.unselectedIcon,
                                contentDescription = navItem.iconTextId,
                            )
                        },
                        label = { Text(navItem.titleTextId) },
                        selected = selected,
                        onClick = { navigator.navigate(navKey) },
                    )
                }
            }
        },
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            val entryProvider =
                entryProvider {
                    habitEntry(navigator)
                    taskEntry(navigator)
                    statsEntry(navigator)
                    profileEntry(navigator)
                    authEntry(navigator)
                }
            NavDisplay(
                entries = navigationState.toEntries(entryProvider),
                onBack = { navigator.goBack() },
            )
        }
    }
}
