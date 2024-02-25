package com.instagram_clone.repos

import android.net.Uri

interface StorageRepository {

    suspend fun postImages(uri: List<Uri>): Resource<List<String>>

}