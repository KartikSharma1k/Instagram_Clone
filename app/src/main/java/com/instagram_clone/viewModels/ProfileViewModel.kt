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

    private val _userDataFlow = MutableStateFlow<Resource<UserData>?>(null)
    val userDataFlow: StateFlow<Resource<UserData>?> = _userDataFlow

    private val _userFollowFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val userFollowFlow: StateFlow<Resource<Boolean>?> = _userFollowFlow

    private val _userUnfollowFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val userUnfollowFlow: StateFlow<Resource<Boolean>?> = _userUnfollowFlow

    fun getPost(uid: String) = viewModelScope.launch {
        _postFlow.value = Resource.Loading
        val result = fireStoreRepository.getPosts(uid)
        _postFlow.value = result
    }

    fun getUserData(uid: String) = viewModelScope.launch {
        _userDataFlow.value = Resource.Loading
        val result = fireStoreRepository.getUserData(uid)
        _userDataFlow.value = result
    }

    fun follow(uid: String) = viewModelScope.launch {
        _userFollowFlow.value = Resource.Loading
        val result = fireStoreRepository.follow(uid)
        _userFollowFlow.value = result
    }

    fun unfollow(uid: String) = viewModelScope.launch {
        _userUnfollowFlow.value = Resource.Loading
        val result = fireStoreRepository.unfollow(uid)
        _userUnfollowFlow.value = result
    }

}