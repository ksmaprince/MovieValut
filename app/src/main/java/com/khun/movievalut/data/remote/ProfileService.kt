package com.khun.movievalut.data.remote

import com.khun.movievalut.data.model.Movie
import com.khun.movievalut.data.model.Profile
import com.khun.movievalut.data.model.UpdatePasswordRequest
import com.khun.movievalut.data.model.UpdatePasswordResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET("profile/{profileId}")
    suspend fun getProfileById(
        @Header("Authorization") token: String,
        @Path("profileId") profileId: Long
    ): Response<Profile>

    @PATCH("profile/addFavourite")
    suspend fun addFavouriteMovie(
        @Header("Authorization") token: String,
        @Query("profileId") profileId: Long,
        @Query("movieId") movieId: Long
    ): Response<Long>

    @GET("profile/favourites/{profileId}")
    suspend fun getAllFavouriteMovies(
        @Header("Authorization") token: String,
        @Path("profileId") profileId: Long
    ): Response<List<Movie>>

    @PUT("profile/{profileId}")
    suspend fun updateProfileById(
        @Header("Authorization") token: String,
        @Path("profileId") profileId: Long,
        @Body profile: Profile
    ): Response<Profile>

    @PATCH("auth/changePassword")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body passwordRequest: UpdatePasswordRequest
    ): Response<UpdatePasswordResponse>

    @Multipart
    @POST("profile/uploadImage/{profileId}")
    suspend fun updateProfileImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Path("profileId") profileId: Long
    ): Response<Profile>
}