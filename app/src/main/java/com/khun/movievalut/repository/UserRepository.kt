package com.khun.movievalut.repository

import android.util.Log
import com.khun.movievalut.data.model.RegisterUserResponse
import com.khun.movievalut.data.model.User
import com.khun.movievalut.data.model.UserLoginRequest
import com.khun.movievalut.data.model.UserLoginResponse
import com.khun.movievalut.data.remote.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {
    suspend fun createUser(user: User): Result<RegisterUserResponse> {
        return try {
            val response = userService.registerUser(user)
            Log.d("Response message() >>>", response.message())
            Log.d("Response code() >>>", response.code().toString())
            Log.d("Response isSuccessful() >>>", response.isSuccessful().toString())
            Log.d("Response errorBody() >>>", response.errorBody().toString())
            Log.d("Response body() >>>", response.body().toString())
            Log.d("Response headers() >>>", response.headers().toString())
            Log.d("Response raw() >>>", response.raw().toString())
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Create user failed"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun userLogin(userLoginRequest: UserLoginRequest): Result<UserLoginResponse> {
        return try {
            val response = userService.loginUser(userLoginRequest)
            Log.d("Response message() >>>", response.message())
            Log.d("Response code() >>>", response.code().toString())
            Log.d("Response isSuccessful() >>>", response.isSuccessful().toString())
            Log.d("Response errorBody() >>>", response.errorBody().toString())
            Log.d("Response body() >>>", response.body().toString())
            Log.d("Response headers() >>>", response.headers().toString())
            Log.d("Response raw() >>>", response.raw().toString())
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                    Result.failure(Exception("Invalid Login"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}