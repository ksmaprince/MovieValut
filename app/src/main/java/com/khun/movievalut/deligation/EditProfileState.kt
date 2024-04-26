package com.khun.movievalut.deligation

import com.khun.movievalut.data.model.Profile

sealed class EditProfileState{
    object Loading: EditProfileState()
    data class Success(val profile: Profile): EditProfileState()
    data class Error(val message: String): EditProfileState()
    object Empty: EditProfileState()
}