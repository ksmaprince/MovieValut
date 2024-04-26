package com.khun.movievalut.deligation

import com.khun.movievalut.data.model.Movie

sealed class FavouriteMovieState {
    object Loading : FavouriteMovieState()
    data class addFavouriteSuccess(val movieId: Long) : FavouriteMovieState()
    data class retrieveFavouriteSuccess(val movies: List<Movie>) : FavouriteMovieState()
    data class Error(val message: String) : FavouriteMovieState()
    object Empty : FavouriteMovieState()
}