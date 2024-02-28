package com.instagram_clone.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.instagram_clone.DataManager
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
class SignUpViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val fireStoreRepository: FireStoreRepository
) : ViewModel() {

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    private val _usernameFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val usernameFlow: StateFlow<Resource<Boolean>?> = _usernameFlow

    private val _addUserFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val addUserFlow: StateFlow<Resource<Boolean>?> = _addUserFlow

    fun createUser(email: String, password: String) = viewModelScope.launch {
        _signUpFlow.value = Resource.Loading
        val result = authRepository.signUp(email, password)
        _signUpFlow.value = result
    }

    fun isUsernameValid(username: String) = viewModelScope.launch {
        _usernameFlow.value = Resource.Loading
        val result = fireStoreRepository.isUsernameValid(username)
        _usernameFlow.value = result
    }

    fun addUser(userData: UserData) = viewModelScope.launch {
        _addUserFlow.value = Resource.Loading
        DataManager.userData = userData
        val result = fireStoreRepository.addUser(userData)
        _addUserFlow.value = result
    }

}