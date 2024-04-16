package com.khun.movievalut.data.remote

import com.khun.movievalut.data.model.User
import com.khun.movievalut.data.model.UserLoginRequest
import com.khun.movievalut.data.model.UserLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("user")
    suspend fun registerUser(@Body user: User): User

    @POST("login")
    suspend fun loginUser(@Body loginRequest: UserLoginRequest): UserLoginResponse
}