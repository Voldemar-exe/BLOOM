package com.example.gamification.model

import androidx.compose.runtime.Immutable
import com.example.model.CustomizationItem

const val COLOR_PRICE = 30

const val AVATAR_PRICE = 40
const val BACKGROUND_PRICE = 50
const val PLANT_PRICE = 100


@Immutable
data class StoreItem(
    val item: CustomizationItem,
    val price: Int,
    val isPurchased: Boolean = false,
)
