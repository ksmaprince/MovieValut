package com.khun.movievalut.data.model

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserLoginResponse(
    val profileId: Long = 0,
    val email: String = "",
    val jwtToken: String = ""
)

data class Role(
    val roleId: Int,
    val roleName: String
)

data class RegisterUserResponse(
    val userId: Long = 0,
    val email: String = "",
    val profile: Profile? = null,
    val role: Role? = null
)