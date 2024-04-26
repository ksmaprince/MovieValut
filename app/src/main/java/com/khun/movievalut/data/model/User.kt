package com.khun.movievalut.data.model

data class User(
    val userId: Long,
    val email: String,
    val password: String,
    val profile: Profile,
    val roleId: Int
)


data class UpdatePasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)

data class UpdatePasswordResponse(val email: String, val message: String)
