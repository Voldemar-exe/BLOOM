package com.example.profile.home

import androidx.compose.runtime.Immutable
import com.example.model.AppSettings
import com.example.model.User
import com.example.model.UserStats

@Immutable
data class ProfileState(
    val user: User? = null,
    val stats: UserStats = UserStats.default(),
    val settings: AppSettings = AppSettings.default(),
    val isLoading: Boolean = true
)
