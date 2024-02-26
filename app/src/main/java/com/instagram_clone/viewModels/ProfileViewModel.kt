package com.instagram_clone.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import com.instagram_clone.repos.AuthRepository
import com.instagram_clone.repos.FireStoreRepository
import com.instagram_clone.repos.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val fireStoreRepository: FireStoreRepository
) :
    ViewModel() {

    private val _postFlow = MutableStateFlow<Resource<List<PostData>>?>(null)
    val postFlow: StateFlow<Resource<List<PostData>>?> = _postFlow

    fun getPost() = viewModelScope.launch {
        _postFlow.value = Resource.Loading
        val result = fireStoreRepository.getPosts(authRepository.currentUser!!.uid)
        _postFlow.value = result
    }

}