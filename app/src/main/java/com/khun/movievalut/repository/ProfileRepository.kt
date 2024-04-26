package com.khun.movievalut.repository

import android.util.Log
import com.khun.movievalut.data.model.Movie
import com.khun.movievalut.data.model.Profile
import com.khun.movievalut.data.model.UpdatePasswordRequest
import com.khun.movievalut.data.model.UpdatePasswordResponse
import com.khun.movievalut.data.remote.ProfileService
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val profileService: ProfileService) {

    suspend fun getProfileById(token: String, profileId: Long): Result<Profile> {
        return try {
            val response = profileService.getProfileById(token, profileId)

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Can't retrieve profile data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFavouriteMovie(token: String, profileId: Long, movieId: Long): Result<Long> {
        return try {
            val response = profileService.addFavouriteMovie(token, profileId, movieId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Can't add favourite movie"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllFavouriteMovie(token: String, profileId: Long): Result<List<Movie>> {
        return try {
            val response = profileService.getAllFavouriteMovies(token, profileId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Can't retrieve favourite movies"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfileById(token: String, profile: Profile): Result<Profile> {
        return try {
            val response = profileService.updateProfileById(token, profile.profileId, profile)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Can't update profile information"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(
        token: String, passwordRequest: UpdatePasswordRequest
    ): Result<UpdatePasswordResponse> {
        return try {
            val response = profileService.changePassword(token, passwordRequest = passwordRequest)
            Log.d("Response message() >>>", response.message())
            Log.d("Response code() >>>", response.code().toString())
            Log.d("Response isSuccessful() >>>", response.isSuccessful().toString())
            Log.d("Response errorBody() >>>", response.errorBody().toString())
            Log.d("Response body() >>>", response.body().toString())
            Log.d("Response headers() >>>", response.headers().toString())
            Log.d("Response raw() >>>", response.raw().toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Can't update your password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}