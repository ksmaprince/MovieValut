package com.khun.movievalut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievalut.data.model.RegisterUserResponse
import com.khun.movievalut.data.model.User
import com.khun.movievalut.deligation.RegisterUserState
import com.khun.movievalut.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _registerUserState = MutableLiveData<RegisterUserState>()
    val registerUserState: LiveData<RegisterUserState> get() = _registerUserState


    fun registerUser(user: User) {
        _registerUserState.value = RegisterUserState.Loading
        viewModelScope.launch {
            val result = userRepository.createUser(user)
            result.onSuccess {
                _registerUserState.value = RegisterUserState.Success(it)
            }
            result.onFailure {
                _registerUserState.value = RegisterUserState.Error(it.message.toString())
            }
        }
    }

    fun setRegisterUserStateEmpty(){
        _registerUserState.value = RegisterUserState.Empty
    }
}