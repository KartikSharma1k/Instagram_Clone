package com.instagram_clone.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.instagram_clone.R

sealed class ProfileTabItem(val title: String, val icon: Int) {
    data object UserFeed : ProfileTabItem(
        title = "Feeds",
        icon = R.drawable.grid_outlined_icon
    )

    data object UserReels : ProfileTabItem(
        title = "Reels",
        icon = R.drawable.instagram_reels_white_icon
    )

    data object UserTags : ProfileTabItem(
        title = "Tags",
        icon = R.drawable.instagram_tag_outline_icon
    )
}