package com.khun.movievalut.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("MOVIE_VAULT", Context.MODE_PRIVATE)
    val TOKEN_KEY: String = "ACCESS_TOKEN"
    val PROFILE_KEY: String = "PROFILE_ID"
    val EMAIL_KEY: String = "EMAIL_ID"

    fun saveAccessToken(token: String?) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAccessToken(): String? {
        return preferences.getString(TOKEN_KEY, null);
    }

    fun saveProfileId(profileId: Long) {
        preferences.edit().putLong(PROFILE_KEY, profileId).apply()

    }

    fun getProfileId(): Long {
        return preferences.getLong(PROFILE_KEY, 0)
    }

    fun saveEmailId(email: String?) {
        preferences.edit().putString(EMAIL_KEY, email).apply()
    }

    fun getEmailId(): String? {
        return preferences.getString(EMAIL_KEY, null)
    }
}