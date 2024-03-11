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
    val comments: Int = 0,
    var tempComments: Int = comments,
    var aspectRatio: Float = 1 / 1f
)
