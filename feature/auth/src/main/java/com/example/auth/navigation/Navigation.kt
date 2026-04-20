package com.example.auth.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.auth.LoginScreen
import com.example.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object AuthNavKey : NavKey

fun EntryProviderScope<NavKey>.authEntry(navigator: Navigator) {
    entry<AuthNavKey> {
        LoginScreen()
    }
}
