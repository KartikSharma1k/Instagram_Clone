package com.instagram_clone.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.instagram_clone.repos.AuthRepository
import com.instagram_clone.repos.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginFLow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFLow

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _loginFLow.value = Resource.Loading
        val result = authRepository.login(email, password)
        _loginFLow.value = result
    }

    init {
        if (authRepository.currentUser != null) {
            _loginFLow.value = Resource.Success(authRepository.currentUser!!)
        }else{
            _loginFLow.value = Resource.Failure(Exception("noUser"))
        }
    }

}