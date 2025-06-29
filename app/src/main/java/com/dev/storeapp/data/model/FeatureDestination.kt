package com.dev.storeapp.data.model

enum class TopPicks {
    MOBILES_ELECTRONICS,
    AUDIO_VIDEO,
    TELEVISIONS,
    ACCOUNT_HELP
}

enum class CategoryDestination {
    MOBILES,
    VIDEO_MUSIC,
    ELECTRONICS_TV,
    GAMING,
    HOME,
    GROCERY,
    KIDS_TOYS,
}

enum class BottomNavDestination {
    ORDERS,
    BUY_AGAIN,
    ACCOUNT,
    LISTS
}

data class TopPicksDestination(
    val title: String,
    val destination: TopPicks,
    val iconRes: Int
)

data class MainCategory(
    val title: String,
    val destination: CategoryDestination,
    val iconRes: Int
)

data class BottomNavItem(
    val title: String,
    val destination: BottomNavDestination
)