package com.example.profile.home

import com.example.model.AppSettings
import com.example.model.User
import com.example.model.UserStats

data class ProfileState(
    val user: User? = null,
    val stats: UserStats? = null,
    val settings: AppSettings = AppSettings.default(),
    val isLoading: Boolean = false
)
