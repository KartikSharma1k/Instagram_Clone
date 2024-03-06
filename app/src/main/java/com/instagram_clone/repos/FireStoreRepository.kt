package com.instagram_clone.repos

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.instagram_clone.models.CommentData
import com.instagram_clone.models.LikeData
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import kotlinx.coroutines.flow.Flow

interface FireStoreRepository {

    suspend fun addUser(userData: UserData): Resource<Boolean>

    suspend fun isUsernameValid(username: String): Resource<Boolean>

    suspend fun getUserData(uId: String): Resource<UserData>

    suspend fun getPosts(uId: String): Resource<List<PostData>>

    suspend fun addPostUrl(postData: PostData): Resource<Boolean>

    suspend fun getFeeds(): Resource<List<PostData>>

    suspend fun getComments(postId: String): Flow<List<CommentData>>

    suspend fun getLikes(postId: String): Resource<List<LikeData>>

    suspend fun addLike(postId: String, uId: String, isAvail: Boolean): Resource<Boolean>

    suspend fun postComment(commentData: CommentData, postId: String, commentCount:Int): Resource<Int>

}