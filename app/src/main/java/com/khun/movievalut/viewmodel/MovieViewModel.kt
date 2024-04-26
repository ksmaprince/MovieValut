package com.khun.movievalut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievalut.data.model.Movie
import com.khun.movievalut.deligation.MovieState
import com.khun.movievalut.repository.MovieRepository
import com.khun.movievalut.ui.util.loginToken
import com.khun.movievalut.ui.util.savedMovies
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel(){

    private val _movieState = MutableLiveData<MovieState>()
    val movieState : MutableLiveData<MovieState> get() = _movieState
    fun fetchAllMovie(){
        _movieState.value = MovieState.Loading
        viewModelScope.launch {
            if (loginToken.isNullOrBlank()){
                _movieState.value = MovieState.Error("Something went wrong with your user validation")
            }else{
                val result = movieRepository.getAllMovies("Bearer $loginToken")
                result.onSuccess {
                    _movieState.value = MovieState.Success(it)
                    savedMovies = it
                }
                result.onFailure {
                    _movieState.value = MovieState.Error(it.message.toString())
                }
            }
        }
    }

    val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    fun setMovieInfo(movie: Movie){
        _movie.value = movie
    }

    fun setMovieStateEmpty(){
        _movieState.value = MovieState.Empty
    }
}