package com.example.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class LeaderboardUser(
    val id: Long,
    val name: String,
    val avatarKey: String,
    val score: Long,
)
