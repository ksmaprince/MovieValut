package com.khun.movievalut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievalut.data.model.User
import com.khun.movievalut.data.model.UserLoginRequest
import com.khun.movievalut.data.model.UserLoginResponse
import com.khun.movievalut.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _userResult = MutableLiveData<Result<User>>()
    val userResult: LiveData<Result<User>> get() = _userResult

    private val _loginResult = MutableLiveData<Result<UserLoginResponse>>()
    val loginResult: LiveData<Result<UserLoginResponse>> get() = _loginResult

    fun registerUser(user: User) {
        viewModelScope.launch {
            _userResult.value = userRepository.createUser(user)
        }
    }

    fun login(userLoginRequest: UserLoginRequest) {
        viewModelScope.launch {
            _loginResult.value = userRepository.userLogin(userLoginRequest)
        }
    }
}