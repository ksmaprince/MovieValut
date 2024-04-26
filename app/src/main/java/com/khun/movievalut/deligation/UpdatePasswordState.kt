package com.khun.movievalut.deligation

import com.khun.movievalut.data.model.UpdatePasswordResponse

sealed class UpdatePasswordState{
    object Loading: UpdatePasswordState()
    data class Success(val updatePasswordResponse: UpdatePasswordResponse): UpdatePasswordState()
    data class Error(val message: String): UpdatePasswordState()
    object Empty: UpdatePasswordState()
}