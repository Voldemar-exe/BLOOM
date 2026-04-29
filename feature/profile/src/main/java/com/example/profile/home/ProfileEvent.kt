package com.example.profile.home

import androidx.navigation3.runtime.NavKey

sealed interface ProfileEvent {
    data class NavigateTo(val destination: NavKey) : ProfileEvent
}
