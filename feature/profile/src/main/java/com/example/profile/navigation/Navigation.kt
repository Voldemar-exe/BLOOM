package com.example.profile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.navigation.Navigator
import com.example.profile.home.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavKey : NavKey

fun EntryProviderScope<NavKey>.profileEntry(navigator: Navigator) {
    entry<ProfileNavKey> {
        ProfileScreen()
    }
}