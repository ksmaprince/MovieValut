package com.khun.movievalut.repository

import com.khun.movievalut.data.model.User
import com.khun.movievalut.data.model.UserLoginRequest
import com.khun.movievalut.data.model.UserLoginResponse
import com.khun.movievalut.data.remote.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {
    suspend fun createUser(user: User): Result<User> {
        return try {
            val response = userService.registerUser(user)
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
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Login Failed"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}