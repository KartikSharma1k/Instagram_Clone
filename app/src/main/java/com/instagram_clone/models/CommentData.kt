package com.instagram_clone.models

import java.util.Date

data class CommentData(
    val commentId: String = "",
    val date: Date = Date(System.currentTimeMillis()),
    val username: String = "",
    val fullname:String = "",
    val profilePic: String = "",
    val text: String = "",
    val uid: String = ""
)