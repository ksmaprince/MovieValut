package com.khun.movievalut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievalut.deligation.FavouriteMovieState
import com.khun.movievalut.repository.ProfileRepository
import com.khun.movievalut.ui.util.loginToken
import com.khun.movievalut.ui.util.userProfileId
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteMovieViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private val _favouriteMovieState = MutableLiveData<FavouriteMovieState>()
    val favouriteMovieState: LiveData<FavouriteMovieState> get() = _favouriteMovieState

    fun addFavoriteMovie(movieId: Long){
        _favouriteMovieState.value = FavouriteMovieState.Loading
        viewModelScope.launch {
            if (userProfileId != 0L && !loginToken.isNullOrBlank()) {
                val result = profileRepository.addFavouriteMovie(
                    token = "Bearer $loginToken",
                    profileId = userProfileId,
                    movieId = movieId
                )
                result.onSuccess {
                    _favouriteMovieState.value = FavouriteMovieState.addFavouriteSuccess(it)
                }

                result.onFailure {
                    _favouriteMovieState.value = FavouriteMovieState.Error(it.message.toString())
                }
            } else {
                _favouriteMovieState.value =
                    FavouriteMovieState.Error("Something went wrong with your user validation")
            }
        }
    }

    fun fetchAllFavouriteMovies(){
        _favouriteMovieState.value = FavouriteMovieState.Loading
        viewModelScope.launch {
            if (userProfileId != 0L && !loginToken.isNullOrBlank()) {
                val result = profileRepository.getAllFavouriteMovie(
                    token = "Bearer $loginToken",
                    profileId = userProfileId
                )
                result.onSuccess {
                    _favouriteMovieState.value = FavouriteMovieState.retrieveFavouriteSuccess(it)
                }

                result.onFailure {
                    _favouriteMovieState.value = FavouriteMovieState.Error(it.message.toString())
                }
            } else {
                _favouriteMovieState.value =
                    FavouriteMovieState.Error("Something went wrong with your user validation")
            }
        }
    }

    fun setFavouriteMovieStateEmpty(){
        _favouriteMovieState.value = FavouriteMovieState.Empty
    }
}