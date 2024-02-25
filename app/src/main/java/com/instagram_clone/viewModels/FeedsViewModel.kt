package com.instagram_clone.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagram_clone.models.CommentData
import com.instagram_clone.models.LikeData
import com.instagram_clone.models.PostData
import com.instagram_clone.repos.FireStoreRepository
import com.instagram_clone.repos.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedsViewModel @Inject constructor(private val fireStoreRepository: FireStoreRepository) :
    ViewModel() {

    private val _feedsFlow = MutableStateFlow<Resource<List<PostData>>?>(null)
    val feedsFlow: StateFlow<Resource<List<PostData>>?> = _feedsFlow

    private val _commentsFlow = MutableStateFlow<Resource<List<CommentData>>?>(null)
    val commentFlow: StateFlow<Resource<List<CommentData>>?> = _commentsFlow

    private val _likeFlow = MutableStateFlow<Resource<List<LikeData>>?>(null)
    val likeFlow: StateFlow<Resource<List<LikeData>>?> = _likeFlow

    fun getFeeds() = viewModelScope.launch {
        _feedsFlow.value = Resource.Loading
        val result = fireStoreRepository.getFeeds()
        _feedsFlow.value = result
    }

    fun getComments(postId: String) = viewModelScope.launch {
        _commentsFlow.value = Resource.Loading
        val result = fireStoreRepository.getComments(postId)
        _commentsFlow.value = result
    }

    fun getLikes(postId: String) = viewModelScope.launch {
        _likeFlow.value = Resource.Loading
        val result = fireStoreRepository.getLikes(postId)
        _likeFlow.value = result
    }

}