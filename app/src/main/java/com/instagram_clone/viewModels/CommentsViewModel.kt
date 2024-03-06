package com.instagram_clone.viewModels

import android.provider.ContactsContract.Data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagram_clone.DataManager
import com.instagram_clone.models.CommentData
import com.instagram_clone.repos.FireStoreRepository
import com.instagram_clone.repos.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.w3c.dom.Comment
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(private val fireStoreRepository: FireStoreRepository) :
    ViewModel() {

    private val _commentFlow = MutableStateFlow<List<CommentData>?>(null)
    var commentFlow: Flow<List<CommentData>?> = _commentFlow

    private val _postCommentFlow = MutableStateFlow<Resource<Int>?>(null)
    val postCommentFlow: StateFlow<Resource<Int>?> = _postCommentFlow

    fun readComments(postId: String) = viewModelScope.launch {
        commentFlow = fireStoreRepository.getComments(postId)
    }

    fun postComment(postId: String, comment: String, commentCount:Int) = viewModelScope.launch {
        val commentId = UUID.randomUUID().toString()

        _postCommentFlow.value = Resource.Loading

        val result = fireStoreRepository.postComment(
            CommentData(
                commentId,
                Date(System.currentTimeMillis()),
                DataManager.userData.username,
                DataManager.userData.fullName,
                DataManager.userData.photoUrl,
                comment,
                DataManager.userData.uid
            ),
            postId,
            commentCount
        )
        _postCommentFlow.value = result

    }

}