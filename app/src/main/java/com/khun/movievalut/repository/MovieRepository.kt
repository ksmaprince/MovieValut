package com.khun.movievalut.repository

import com.khun.movievalut.data.model.Movie
import com.khun.movievalut.data.remote.MovieService
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {
    suspend fun getAllMovies(token: String): Result<List<Movie>> {
        return try {
            val response = movieService.getAllMovies(token)

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Fail to load data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}