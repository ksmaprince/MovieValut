package com.khun.movievalut.data.remote

import com.khun.movievalut.data.model.User
import com.khun.movievalut.data.model.UserLoginRequest
import com.khun.movievalut.data.model.UserLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("auth/addUser")
    suspend fun registerUser(@Body user: User): Response<User>

    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: UserLoginRequest): Response<UserLoginResponse>
}