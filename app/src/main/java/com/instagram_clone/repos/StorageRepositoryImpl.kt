package com.instagram_clone.repos

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) :
    StorageRepository {

    override suspend fun postImages(uri: List<Uri>): Resource<List<String>> {
        return try {
            val urlList = arrayListOf<String>()
            for (i in uri.indices) {
                val id = UUID.randomUUID().toString()
                val ref = storage.getReference("posts").child(firebaseAuth.currentUser!!.uid)
                    .child(id)
                ref.putFile(uri[i]).await()
                urlList.add(ref.downloadUrl.await().toString())
            }
            Resource.Success(urlList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}