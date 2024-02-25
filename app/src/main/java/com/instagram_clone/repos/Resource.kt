package com.instagram_clone.repos

import androidx.compose.runtime.State
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

sealed class Resource<out R> {
    data class Success<out R>(val result: R) : Resource<R>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}