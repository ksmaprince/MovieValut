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

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){

    private var _user = MutableLiveData<User>()
    val user get() = _user

    private var _userLoginResponse = MutableLiveData<UserLoginResponse>()
    val userLoginResponse get() = _userLoginResponse

    fun registerUser(user: User) {
        viewModelScope.launch {
            _user.value = userRepository.createUser(user)
        }
    }

    fun login(userLoginRequest: UserLoginRequest){
        viewModelScope.launch {
            _userLoginResponse.value = userRepository.userLogin(userLoginRequest)
        }
    }
}