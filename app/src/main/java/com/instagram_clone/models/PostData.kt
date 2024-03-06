package com.instagram_clone.models

import java.util.Date

data class PostData(
    val username: String = "",
    val postId: String = "",
    val uid: String = "",
    val profileImage: String = "",
    val photoUrl: List<String> = emptyList(),
    val datePublished: Date = Date(System.currentTimeMillis()),
    val description: String = "",
    val likes: ArrayList<String> = ArrayList(),
    var comments: Int = 0
)
