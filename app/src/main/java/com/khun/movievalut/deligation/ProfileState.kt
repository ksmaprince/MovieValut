package com.khun.movievalut.deligation

import com.khun.movievalut.data.model.Profile

sealed class ProfileState{
    object Loading: ProfileState()
    data class Success(val profile: Profile): ProfileState()
    data class Error(val message: String): ProfileState()
    object Empty: ProfileState()
}