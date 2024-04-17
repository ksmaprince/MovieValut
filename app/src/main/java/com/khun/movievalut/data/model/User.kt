package com.khun.movievalut.data.model

data class User(
    val userId: Long,
    val email: String,
    val password: String,
    val profile: Profile,
    val roleIds: List<Int>
)
