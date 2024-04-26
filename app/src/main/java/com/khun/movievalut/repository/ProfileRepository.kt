package com.khun.movievalut.repository

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
                Result.failure(Exception("You may already added. Can't add it at the moment"))
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