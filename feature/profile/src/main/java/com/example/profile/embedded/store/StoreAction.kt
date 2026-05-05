package com.example.profile.embedded.store

interface StoreAction {
    data class PurchaseColor(val colorKey: String) : StoreAction

    data class PurchaseBackground(val backgroundKey: String) : StoreAction

    data class PurchasePlant(val plantKey: String) : StoreAction
}
