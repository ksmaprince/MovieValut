package com.khun.movievalut.data.model

data class UserLoginRequest(
    val email: String,
    val password: String
)
data class UserLoginResponse(
    val userId: Long,
    val email: String,
    val password: String,
    val token: String,
    val refreshToken: String
)