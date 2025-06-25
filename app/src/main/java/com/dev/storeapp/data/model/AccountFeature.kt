package com.dev.storeapp.data.model

data class AccountFeature(
    val title: String,
    val destination: FeatureDestination
)

enum class FeatureDestination {
    BUY_AGAIN, RECENT_ORDERS, PROFILE, ORDERS, CARTS
}

