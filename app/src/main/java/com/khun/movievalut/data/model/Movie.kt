package com.khun.movievalut.data.model

data class Movie(
    val movieId: Long,
    val movieTitle: String,
    val overview: String,
    val poster: String,
    val rating: Double,
    val releaseDate: String,
    val trailer: String
)