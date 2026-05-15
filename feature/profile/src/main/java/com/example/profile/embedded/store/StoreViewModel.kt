package com.example.profile.embedded.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.picture.BloomColors
import com.example.designsystem.picture.BloomPlants
import com.example.gamification.model.AVATAR_PRICE
import com.example.gamification.model.BACKGROUND_PRICE
import com.example.gamification.model.COLOR_PRICE
import com.example.gamification.model.PLANT_PRICE
import com.example.gamification.model.StoreItem
import com.example.model.CustomizationItem
import com.example.model.CustomizationType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StoreViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val baseState =
        StoreState(
            colors =
                BloomColors.entries.map {
                    StoreItem(
                        item = CustomizationItem(it.name, CustomizationType.COLOR),
                        price = COLOR_PRICE,
                    )
                },
            avatars =
                BloomAvatars.entries.map {
                    StoreItem(
                        item = CustomizationItem(it.name, CustomizationType.AVATAR),
                        price = AVATAR_PRICE,
                    )
                },
            backgrounds =
                BloomBackgrounds.entries.map {
                    StoreItem(
                        item = CustomizationItem(it.name, CustomizationType.BACKGROUND),
                        price = BACKGROUND_PRICE,
                    )
                },
            plants =
                BloomPlants.entries.map {
                    StoreItem(
                        item = CustomizationItem(it.name, CustomizationType.PLANT),
                        price = PLANT_PRICE,
                    )
                },
            currency = 0,
        )

    val state: StateFlow<StoreState> =
        userRepository.user
            .map { user ->
                val ownedItems = user?.ownedItems.orEmpty()
                val currency = userRepository.stats.first().currentCoinsAmount

                baseState.copy(
                    avatars = baseState.avatars.markPurchased(ownedItems),
                    colors = baseState.colors.markPurchased(ownedItems),
                    backgrounds = baseState.backgrounds.markPurchased(ownedItems),
                    plants = baseState.plants.markPurchased(ownedItems),
                    currency = currency,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = baseState,
            )

    fun onAction(event: StoreAction) {
        val purchase =
            when (event) {
                is StoreAction.PurchaseColor ->
                    PurchaseData(
                        key = event.colorKey,
                        type = CustomizationType.COLOR,
                        price = COLOR_PRICE,
                    )

                is StoreAction.PurchaseBackground ->
                    PurchaseData(
                        key = event.backgroundKey,
                        type = CustomizationType.BACKGROUND,
                        price = BACKGROUND_PRICE,
                    )

                is StoreAction.PurchasePlant ->
                    PurchaseData(
                        key = event.plantKey,
                        type = CustomizationType.PLANT,
                        price = PLANT_PRICE,
                    )

                is StoreAction.PurchaseAvatar ->
                    PurchaseData(
                        key = event.avatarKey,
                        type = CustomizationType.AVATAR,
                        price = AVATAR_PRICE,
                    )
            }

        purchaseItem(purchase)
    }

    private fun List<StoreItem>.markPurchased(
        ownedItems: List<CustomizationItem>,
    ): List<StoreItem> =
        map { item ->
            item.copy(isPurchased = item.item in ownedItems)
        }

    private fun purchaseItem(purchase: PurchaseData) {
        if (state.value.currency < purchase.price) return

        viewModelScope.launch {
            userRepository.addPurchase(purchase.key, purchase.type, purchase.price)
        }
    }

    private data class PurchaseData(
        val key: String,
        val type: CustomizationType,
        val price: Int,
    )
}
