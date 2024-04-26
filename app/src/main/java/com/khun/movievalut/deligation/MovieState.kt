package com.khun.movievalut.deligation

import com.khun.movievalut.data.model.Movie

sealed class MovieState{
    object Loading: MovieState()
    data class Success(val movies: List<Movie>): MovieState()
    data class Error(val message: String): MovieState()
    object Empty: MovieState()
}