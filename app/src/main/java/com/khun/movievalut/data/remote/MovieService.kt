package com.khun.movievalut.data.remote

import com.khun.movievalut.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MovieService {

    @GET("movie/list")
    suspend fun getAllMovies(@Header("Authorization") token: String) : Response<List<Movie>>
}