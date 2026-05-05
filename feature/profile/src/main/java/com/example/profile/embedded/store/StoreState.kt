package com.example.profile.embedded.store

import androidx.compose.runtime.Immutable
import com.example.gamification.model.StoreItem

@Immutable
data class StoreState(
    val colors: List<StoreItem> = emptyList(),
    val backgrounds: List<StoreItem> = emptyList(),
    val plants: List<StoreItem> = emptyList(),
    val currency: Int = 0,
)
