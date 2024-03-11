package com.instagram_clone.viewModels

import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagram_clone.models.PostData
import com.instagram_clone.repos.FireStoreRepository
import com.instagram_clone.repos.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val fireStoreRepository: FireStoreRepository) :
    ViewModel() {

    private val _gridFlow = MutableStateFlow<Resource<List<PostData>>?>(null)
    val gridFlow: StateFlow<Resource<List<PostData>>?> = _gridFlow

    fun getGridData() = viewModelScope.launch {
        _gridFlow.value = Resource.Loading
        val result: Resource<List<PostData>> = fireStoreRepository.getFeeds()
        var counter = 1
        result.let {
            when (it) {
                is Resource.Success -> {
                    for (i in it.result.indices) {
                        if (counter > 10) counter = 1

                        if (counter == 3 || counter == 6) it.result[i].aspectRatio = 1 / 2f

                        counter++
                    }
                }

                is Resource.Loading -> {}

                is Resource.Failure -> {}
            }
        }

        _gridFlow.value = result

    }

}