package com.instagram_clone.repos


import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObjects
import com.instagram_clone.DataManager
import com.instagram_clone.models.CommentData
import com.instagram_clone.models.LikeData
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.log


class FireStoreRepositoryImpl @Inject constructor(val fireStore: FirebaseFirestore) :
    FireStoreRepository {

    override suspend fun addUser(userData: UserData): Resource<Boolean> {
        return try {
            fireStore.collection("users").document(userData.uid).set(userData).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun isUsernameValid(username: String): Resource<Boolean> {
        return try {
            val result =
                fireStore.collection("users").whereEqualTo("username", username).get().await()
            if (result.size() > 0) {
                Resource.Success(false)
            } else
                Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserData(uId: String): Resource<UserData> {
        return try {
            val result: DocumentSnapshot = fireStore.collection("users").document(uId).get().await()
            Resource.Success(result.toObject(UserData::class.java)!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getPosts(uId: String): Resource<List<PostData>> {
        return try {
            val result: QuerySnapshot =
                fireStore.collection("posts").whereEqualTo("uid", uId)
                    .orderBy("datePublished", Query.Direction.DESCENDING).get().await()
            Resource.Success(result.toObjects(PostData::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addPostUrl(
        postData: PostData
    ): Resource<Boolean> {
        return try {
            fireStore.collection("posts").document(postData.postId).set(postData).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getFeeds(): Resource<List<PostData>> {
        return try {
            val feedResult = fireStore.collection("posts").get().await()
            Resource.Success(feedResult.toObjects(PostData::class.java).shuffled())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getComments(postId: String): Flow<List<CommentData>> {
        return callbackFlow {
            val result =
                fireStore.collection("posts").document(postId).collection("comments")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.d("error", "getComments: error - ${error.message}")
                            close(error)
                        } else {
                            if (snapshot != null) {
                                trySend(snapshot.toObjects(CommentData::class.java)).isSuccess
                            }
                        }
                    }
            awaitClose { result.remove() }
        }
    }

    override suspend fun getLikes(postId: String): Resource<List<LikeData>> {
        return try {
            val result =
                fireStore.collection("posts").document(postId).collection("likes").get().await()
            Resource.Success(result.toObjects(LikeData::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addLike(
        postId: String,
        uId: String,
        isAvail: Boolean
    ): Resource<Boolean> {
        return try {
            if (isAvail)
                fireStore.collection("posts").document(postId)
                    .update("likes", FieldValue.arrayRemove(uId)).await()
            else
                fireStore.collection("posts").document(postId)
                    .update("likes", FieldValue.arrayUnion(uId)).await()

            Resource.Success(true)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun postComment(
        commentData: CommentData,
        postId: String,
        commentCount: Int
    ): Resource<Int> {
        return try {
            var count = commentCount
            ++count
            Log.d("commentCount", "postComment: count - $commentCount")
            fireStore.collection("posts").document(postId).update("comments", count).await()

            fireStore.collection("posts").document(postId).collection("comments")
                .document(commentData.commentId).set(commentData).await()
            Resource.Success(count)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getSearchQuery(query: String): Resource<List<UserData>> {
        return try {
            val userName =
                fireStore.collection("users").whereGreaterThanOrEqualTo("username", query)
                    .get().await()
            val result =
                fireStore.collection("users").whereGreaterThanOrEqualTo("fullName", query)
                    .get().await()

            val list = userName.toObjects(UserData::class.java)
            list.addAll(result.toObjects(UserData::class.java))

            Resource.Success(list.distinct())

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun follow(uid: String): Resource<Boolean> {
        return try {
            fireStore.collection("users").document(DataManager.userData.uid)
                .update("following", FieldValue.arrayUnion(uid)).await()

            fireStore.collection("users").document(uid)
                .update("followers", FieldValue.arrayUnion(DataManager.userData.uid)).await()

            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun unfollow(uid: String): Resource<Boolean> {
        return try {
            fireStore.collection("users").document(DataManager.userData.uid)
                .update("following", FieldValue.arrayRemove(uid)).await()

            fireStore.collection("users").document(uid)
                .update("followers", FieldValue.arrayRemove(DataManager.userData.uid)).await()

            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}

