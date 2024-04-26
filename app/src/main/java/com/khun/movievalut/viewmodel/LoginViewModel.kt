package com.khun.movievalut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievalut.data.local.PreferencesManager
import com.khun.movievalut.data.model.UserLoginRequest
import com.khun.movievalut.data.model.UserLoginResponse
import com.khun.movievalut.deligation.LoginState
import com.khun.movievalut.repository.UserRepository
import com.khun.movievalut.ui.util.loginToken
import com.khun.movievalut.ui.util.userEmail
import com.khun.movievalut.ui.util.userProfileId
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    @Inject
    lateinit var preferencesManager: PreferencesManager


    private var _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    fun login(userLoginRequest: UserLoginRequest) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = userRepository.userLogin(userLoginRequest)
            result.onSuccess {
                preferencesManager.saveAccessToken(it.jwtToken)
                preferencesManager.saveEmailId(it.email)
                preferencesManager.saveProfileId(it.profileId)
                _loginState.value = LoginState.Success(it)
            }
            result.onFailure {
                _loginState.value = LoginState.Error(it.message.toString())
            }
        }
    }

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    private var _userLoginResponse = MutableLiveData<UserLoginResponse>()
    val userLoginResponse: LiveData<UserLoginResponse> get() = _userLoginResponse

    fun getToken() {
        viewModelScope.launch {
            userProfileId = preferencesManager.getProfileId()
            preferencesManager.getEmailId()?.let {
                userEmail = it
            }
            preferencesManager.getAccessToken()?.let {
                loginToken = it
            }

            if (userProfileId != 0L && !userEmail.isNullOrBlank() && !loginToken.isNullOrBlank()) {
                _userLoginResponse.value =
                    UserLoginResponse(userProfileId, userEmail!!, loginToken!!)
                _token.value = loginToken
            }
        }
    }

    fun resetState() {
        preferencesManager.saveEmailId(null)
        preferencesManager.saveAccessToken(null)
        preferencesManager.saveProfileId(0)
        userProfileId = 0
        userEmail = null
        loginToken = null
        _loginState = MutableLiveData<LoginState>()
        _token = MutableLiveData<String>()
        _userLoginResponse.value = UserLoginResponse()
        _userLoginResponse = MutableLiveData<UserLoginResponse>()
    }

    fun setLoginScreenEmpty(){
        _loginState.value = LoginState.Empty
    }
}