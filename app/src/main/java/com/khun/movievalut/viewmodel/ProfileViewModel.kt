package com.khun.movievalut.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievalut.data.model.Profile
import com.khun.movievalut.data.model.UpdatePasswordRequest
import com.khun.movievalut.deligation.EditProfileState
import com.khun.movievalut.deligation.ProfileState
import com.khun.movievalut.deligation.UpdatePasswordState
import com.khun.movievalut.repository.ProfileRepository
import com.khun.movievalut.ui.util.loginToken
import com.khun.movievalut.ui.util.userEmail
import com.khun.movievalut.ui.util.userProfileId
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> get() = _profileState

    fun getUserProfile() {
        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            if (userProfileId != 0L && !loginToken.isNullOrBlank()) {
                val profileResult = profileRepository.getProfileById(
                    token = "Bearer $loginToken",
                    profileId = userProfileId
                )
                profileResult.onSuccess {
                    _profileState.value = ProfileState.Success(it)
                }

                profileResult.onFailure {
                    _profileState.value = ProfileState.Error(it.message.toString())
                }
            } else {
                _profileState.value =
                    ProfileState.Error("Something went wrong with your user validation")
            }
        }
    }


    private val _editProfileState = MutableLiveData<EditProfileState>()
    val editProfileState: LiveData<EditProfileState> get() = _editProfileState
    fun updateUserProfile(profile: Profile) {
        _editProfileState.value = EditProfileState.Loading
        viewModelScope.launch {
            if (userProfileId != 0L && !loginToken.isNullOrBlank()) {
                val profileResult = profileRepository.updateProfileById(
                    token = "Bearer $loginToken",
                    profile = profile
                )
                profileResult.onSuccess {
                    _editProfileState.value = EditProfileState.Success(it)
                }

                profileResult.onFailure {
                    _editProfileState.value = EditProfileState.Error(it.message.toString())
                }
            } else {
                _editProfileState.value =
                    EditProfileState.Error("Something went wrong with your user validation")
            }
        }
    }

    private val _updatePasswordState = MutableLiveData<UpdatePasswordState>()
    val updatePasswordState: LiveData<UpdatePasswordState> get() = _updatePasswordState

    fun updatePassword(currentPassword: String, newPassword: String) {
        _updatePasswordState.value = UpdatePasswordState.Loading
        viewModelScope.launch {
            if (userProfileId != 0L && !loginToken.isNullOrBlank() && !userEmail.isNullOrBlank()) {

                val result = profileRepository.updatePassword(
                    token = "Bearer $loginToken",
                    UpdatePasswordRequest(
                        email = userEmail!!,
                        currentPassword = currentPassword,
                        newPassword = newPassword
                    )
                )

                result.onSuccess {
                    _updatePasswordState.value = UpdatePasswordState.Success(it)
                }
                result.onFailure {
                    _updatePasswordState.value = UpdatePasswordState.Error(it.message.toString())
                }
            } else {
                _updatePasswordState.value =
                    UpdatePasswordState.Error("Something went wrong with your user validation")
            }
        }
    }
    fun uploadeImage(file: File) {
        _profileState.value = ProfileState.Loading
        Log.d("Upload Image>>> ", "Starting ....")
        viewModelScope.launch {
            if (userProfileId != 0L && !loginToken.isNullOrBlank()) {

                val requestFile: RequestBody = RequestBody.create(
                    "image/jpg".toMediaType(),
                    file
                )

                val result = profileRepository.updateProfileImage(
                    token = "Bearer $loginToken",
                    profileId = userProfileId,
                    file = MultipartBody.Part.createFormData(
                        name = "file",
                        filename = "${userProfileId}_image_${file.name}",
                        body = requestFile
                    )
                )

                result.onSuccess {
                    Log.d("Upload Image>>> ", "Success ....")
                    _profileState.value = ProfileState.Success(it)
                }
                result.onFailure {
                    Log.d("Upload Image>>> ", "Fail With....${it.message}")
                    _profileState.value = ProfileState.Error(it.message.toString())
                }
            } else {
                _profileState.value =
                    ProfileState.Error("Something went wrong with your user validation")
            }
        }
    }

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    fun setProfile(profile: Profile) {
        _profile.value = profile
    }

    fun setProfileStateEmpty() {
        _profileState.value = ProfileState.Empty
    }

    fun setEditProfileStateEmpty() {
        _editProfileState.value = EditProfileState.Empty
    }

    fun setUpdatePasswordStateEmpty() {
        _updatePasswordState.value = UpdatePasswordState.Empty
    }

}