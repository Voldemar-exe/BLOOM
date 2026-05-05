package com.example.profile.embedded.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.picture.BloomColors
import com.example.designsystem.picture.BloomPlants
import com.example.gamification.model.BACKGROUND_PRICE
import com.example.gamification.model.COLOR_PRICE
import com.example.gamification.model.PLANT_PRICE
import com.example.gamification.model.StoreItem
import com.example.model.CustomizationItem
import com.example.model.CustomizationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow(StoreState())
    val state: StateFlow<StoreState> =
        combine(
            _state,
            userRepository.user,
        ) { newState, user ->
            val currency = userRepository.stats.first().currentCoinsAmount
            val ownedItems = user!!.ownedItems
            StoreState(
                colors = newState.colors.map { it.copy(isPurchased = it.item in ownedItems) },
                backgrounds =
                    newState.backgrounds.map {
                        it.copy(
                            isPurchased = it.item in ownedItems,
                        )
                    },
                plants = newState.plants.map { it.copy(isPurchased = it.item in ownedItems) },
                currency = currency,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StoreState(),
        )

    init {
        loadStoreItems()
    }

    private fun loadStoreItems() {
        viewModelScope.launch {
            val colors =
                BloomColors.entries.map {
                    StoreItem(
                        item = CustomizationItem(it.name, CustomizationType.COLOR),
                        price = COLOR_PRICE,
                    )
                }
            val backgrounds =
                BloomBackgrounds.entries.map {
                    StoreItem(
                        item = CustomizationItem(it.name, CustomizationType.BACKGROUND),
                        price = BACKGROUND_PRICE,
                    )
                }
            val plants =
                BloomPlants.entries.map {
                    StoreItem(
                        item = CustomizationItem(it.name, CustomizationType.PLANT),
                        price = PLANT_PRICE,
                    )
                }
            val currency = userRepository.stats.first().currentCoinsAmount

            _state.update {
                it.copy(
                    colors = colors,
                    backgrounds = backgrounds,
                    plants = plants,
                    currency = currency,
                )
            }
        }
    }

    fun onAction(event: StoreAction) {
        when (event) {
            is StoreAction.PurchaseColor -> {
                if (_state.value.currency < COLOR_PRICE) return
                purchaseColor(event.colorKey)
            }

            is StoreAction.PurchaseBackground -> {
                if (_state.value.currency < BACKGROUND_PRICE) return
                purchaseBackground(event.backgroundKey)
            }

            is StoreAction.PurchasePlant -> {
                if (_state.value.currency < PLANT_PRICE) return
                purchasePlant(event.plantKey)
            }
        }
    }

    private fun purchaseColor(colorKey: String) {
        viewModelScope.launch {
            userRepository.addPurchase(colorKey, CustomizationType.COLOR, COLOR_PRICE)
        }
    }

    private fun purchaseBackground(backgroundKey: String) {
        viewModelScope.launch {
            userRepository.addPurchase(backgroundKey, CustomizationType.BACKGROUND, BACKGROUND_PRICE)
        }
    }

    private fun purchasePlant(plantKey: String) {
        viewModelScope.launch {
            userRepository.addPurchase(plantKey, CustomizationType.COLOR, PLANT_PRICE)
        }
    }
}
