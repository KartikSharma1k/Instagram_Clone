package com.instagram_clone.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import com.instagram_clone.repos.AuthRepository
import com.instagram_clone.repos.FireStoreRepository
import com.instagram_clone.repos.Resource
import com.instagram_clone.repos.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val fireStoreRepository: FireStoreRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {

    private val _userDataFlow = MutableStateFlow<Resource<UserData>?>(null)
    val userData: StateFlow<Resource<UserData>?> = _userDataFlow

    private val _addPostUrl = MutableStateFlow<Resource<Boolean>?>(null)
    val addPostFlow: StateFlow<Resource<Boolean>?> = _addPostUrl

    private val _storageFlow = MutableStateFlow<Resource<String>?>(null)
    val storageFlow: StateFlow<Resource<String>?> = _storageFlow

    fun addPost(uri: List<Uri>, description: String) = viewModelScope.launch {
        _storageFlow.value = Resource.Loading
        _addPostUrl.value = Resource.Loading
        val result = storageRepository.postImages(uri)
        result.let {
            when (it) {
                is Resource.Success -> {
                    getUser(it.result, description)
                }

                Resource.Loading -> _storageFlow.value = Resource.Loading

                is Resource.Failure -> {
                    _storageFlow.value = it
                }
            }
        }
    }

    private fun getUser(urlList: List<String>, description: String) = viewModelScope.launch {
        _userDataFlow.value = Resource.Loading
        val result = fireStoreRepository.getUserData(authRepository.currentUser!!.uid)
        result.let {
            when (it) {
                is Resource.Success -> {
                    addPostUrl(it.result, urlList, description)
                }

                Resource.Loading -> _userDataFlow.value = Resource.Loading

                is Resource.Failure -> {
                    _userDataFlow.value = it
                }
            }
        }
    }

    private fun addPostUrl(userData: UserData, urlList: List<String>, description: String) =
        viewModelScope.launch {
            val postId = UUID.randomUUID().toString()
            _addPostUrl.value = Resource.Loading
            val result = fireStoreRepository.addPostUrl(
                PostData(
                    userData.username, postId, userData.uid, userData.photoUrl, urlList,
                    Date(System.currentTimeMillis()), description
                )
            )
            _addPostUrl.value = result
        }
}