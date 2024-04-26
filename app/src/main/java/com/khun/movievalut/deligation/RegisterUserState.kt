package com.khun.movievalut.deligation

import com.khun.movievalut.data.model.RegisterUserResponse
sealed class RegisterUserState {
    object Loading : RegisterUserState()
    data class Success(val registerUserResponse: RegisterUserResponse) : RegisterUserState()
    data class Error(val message: String) : RegisterUserState()
    object Empty: RegisterUserState()
}