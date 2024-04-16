package com.khun.movievalut.repository

import com.khun.movievalut.data.model.User
import com.khun.movievalut.data.model.UserLoginRequest
import com.khun.movievalut.data.model.UserLoginResponse
import com.khun.movievalut.data.remote.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {
    suspend fun createUser(user: User): User = userService.registerUser(user)
    suspend fun userLogin(userLoginRequest: UserLoginRequest): UserLoginResponse =
        userService.loginUser(userLoginRequest)
}