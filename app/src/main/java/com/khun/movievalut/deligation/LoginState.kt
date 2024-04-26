package com.khun.movievalut.deligation

import com.khun.movievalut.data.model.UserLoginResponse

sealed class LoginState{
    object Loading: LoginState()
    data class Success(val userLoginResponse: UserLoginResponse): LoginState()
    data class Error(val message: String): LoginState()
    object Empty: LoginState()
}