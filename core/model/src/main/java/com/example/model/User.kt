package com.example.model

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val userId: Long,
    val email: String,
    val username: String,
    val avatarKey: String,
    val backgroundKey: String,
    val colorKey: String,
    val ownedAchievements: Set<Int>,
    val ownedItems: List<CustomizationItem>
)
