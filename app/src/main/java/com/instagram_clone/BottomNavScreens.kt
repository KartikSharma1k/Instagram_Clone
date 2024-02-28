package com.instagram_clone

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home

sealed class BottomNavScreens(
    val title: String,
    var iconSelected: Int = R.drawable.instagram_home_icon,
    var iconNotSelected: Int = R.drawable.instagram_home_outlined_icon,
    val route: String
) {
    data object Home : BottomNavScreens(
        title = "Home",
        iconSelected = R.drawable.instagram_home_icon,
        iconNotSelected = R.drawable.instagram_home_outlined_icon,
        route = BottomNavRoutes.HOME.route
    )

    data object Search : BottomNavScreens(
        title = "Search",
        iconSelected = R.drawable.instagram_search_filled_icon,
        iconNotSelected = R.drawable.instagram_search_outlined_icon,
        route = BottomNavRoutes.SEARCH.route
    )

    data object AddPost : BottomNavScreens(
        title = "AddPost",
        iconSelected = R.drawable.instagram_add_filled_icon,
        iconNotSelected = R.drawable.instagram_add_icon,
        route = BottomNavRoutes.ADDPOST.route
    )

    data object Reels : BottomNavScreens(
        title = "Reels",
        iconSelected = R.drawable.instagram_reels_filled_icon,
        iconNotSelected = R.drawable.instagram_reels_white_icon,
        route = BottomNavRoutes.REELS.route
    )

    data object Profile : BottomNavScreens(title = "Profile", route = BottomNavRoutes.PROFILE.route)
    data object Notification : BottomNavScreens(
        title = "Notification",
        iconSelected = R.drawable.instagram_favourite_filled_icon,
        iconNotSelected = R.drawable.instagram_favourite_outlined_icon,
        route = BottomNavRoutes.NOTIFICATION.route
    )

    data object Chat : BottomNavScreens(
        title = "Chat",
        iconSelected = R.drawable.instagram_chat_icon,
        iconNotSelected = R.drawable.instagram_chat_icon,
        route = BottomNavRoutes.CHAT.route
    )

}

enum class BottomNavRoutes(val route: String) {
    HOME("home"),
    SEARCH("search"),
    ADDPOST("addPost"),
    REELS("reels"),
    PROFILE("profile"),
    NOTIFICATION("notification"),
    CHAT("chat"),
    COMMENTS("comments")
}