package com.instagram_clone.repos

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.instagram_clone.models.CommentData
import com.instagram_clone.models.LikeData
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
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
            val postId = UUID.randomUUID()
            fireStore.collection("posts").document(postId.toString()).set(postData).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getFeeds(): Resource<List<PostData>> {
        return try {
            val feedResult = fireStore.collection("posts").get().await()
            Resource.Success(feedResult.toObjects(PostData::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getComments(postId: String): Resource<List<CommentData>> {
        return try {
            val result =
                fireStore.collection("posts").document(postId).collection("comments").get().await()
            Resource.Success(result.toObjects(CommentData::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
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
}

/*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun listenToFirestoreChanges(collection: String, document: String): Flow<DocumentSnapshot> {
    return callbackFlow {
        val listener = FirebaseFirestore.getInstance().collection(collection).document(document).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
            } else {
                if (snapshot != null) {
                    offer(snapshot)
                }
            }
        }
        awaitClose { listener.remove() }
    }
}

fun listenToFirestoreChanges(collection: String): Flow<QuerySnapshot> {
    return callbackFlow {
        val listener = FirebaseFirestore.getInstance().collection(collection).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
            } else {
                if (snapshot != null) {
                    offer(snapshot)
                }
            }
        }
        awaitClose { listener.remove() }
    }
}
*/