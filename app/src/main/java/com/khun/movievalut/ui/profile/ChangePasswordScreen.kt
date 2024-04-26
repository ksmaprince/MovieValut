package com.khun.movievalut.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khun.movievalut.Destinations
import com.khun.movievalut.R
import com.khun.movievalut.deligation.UpdatePasswordState
import com.khun.movievalut.ui.login.ConfirmPasswordState
import com.khun.movievalut.ui.login.Password
import com.khun.movievalut.ui.login.PasswordState
import com.khun.movievalut.ui.util.SecretKey
import com.khun.movievalut.ui.util.encryptPassword
import com.khun.movievalut.ui.util.showDialog
import com.khun.movievalut.ui.util.showLoadingDialog
import com.khun.movievalut.viewmodel.LoginViewModel
import com.khun.movievalut.viewmodel.ProfileViewModel

@Composable
fun ChangePasswordScreen(loginViewModel: LoginViewModel, profileViewModel: ProfileViewModel, navController: NavController) {
    Scaffold(topBar = {
        EditProfileTopAppBar(
            topAppBarText = stringResource(id = R.string.change_password),
            onNavUp = {
                profileViewModel.setUpdatePasswordStateEmpty()
                navController.popBackStack()
            },
        )
    }, content = { contentPadding ->
        Column(Modifier.padding(contentPadding)) {

            val passwordFocusRequest = remember { FocusRequester() }
            val currentPasswordState = remember {
                PasswordState()
            }
            val confirmationPasswordFocusRequest = remember { FocusRequester() }
            val passwordState = remember { PasswordState() }
            val confirmPasswordState =
                remember { ConfirmPasswordState(passwordState = passwordState) }

            var isLoading by remember {
                mutableStateOf(false)
            }

            var isShowErrorDialog by remember {
                mutableStateOf(false)
            }

            var isShowSuccessDialog by remember {
                mutableStateOf(false)
            }
            var successMessage by remember {
                mutableStateOf("")
            }
            var errorMessage by remember {
                mutableStateOf("")
            }

            profileViewModel.updatePasswordState.observeForever {
                when(it){
                    is UpdatePasswordState.Loading -> {
                        isLoading = true
                        isShowSuccessDialog= false
                        isShowErrorDialog = false
                    }
                    is UpdatePasswordState.Success -> {
                        successMessage = "${it.updatePasswordResponse.message}. Please Login again"
                        profileViewModel.setUpdatePasswordStateEmpty()
                        isLoading = false
                        isShowSuccessDialog= true
                        isShowErrorDialog = false
                    }
                    is UpdatePasswordState.Error -> {
                        errorMessage = it.message
                        profileViewModel.setUpdatePasswordStateEmpty()
                        isLoading = false
                        isShowSuccessDialog= false
                        isShowErrorDialog = true
                    }
                    is UpdatePasswordState.Empty -> {

                    }
                }
            }

            val onSubmit = {
                val currentPassword = encryptPassword(SecretKey, currentPasswordState.text)
                val newPassword = encryptPassword(SecretKey, passwordState.text)

                profileViewModel.updatePassword(currentPassword, newPassword)
            }


            Password(
                label = stringResource(R.string.current_password),
                passwordState = currentPasswordState,
                imeAction = ImeAction.Next,
                onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
                modifier = Modifier
                    .focusRequester(passwordFocusRequest)
                    .padding(10.dp)
            )


            Password(
                label = stringResource(R.string.new_password),
                passwordState = passwordState,
                imeAction = ImeAction.Next,
                onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
                modifier = Modifier
                    .focusRequester(passwordFocusRequest)
                    .padding(10.dp)
            )

            Password(
                label = stringResource(R.string.confirm_new_password),
                passwordState = confirmPasswordState,
                onImeAction = {
                    onSubmit()
                },
                modifier = Modifier
                    .focusRequester(confirmationPasswordFocusRequest)
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSubmit()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                enabled = passwordState.isValid && confirmPasswordState.isValid
            ) {
                Text(text = stringResource(R.string.change_password))
            }

            if (isLoading){
                showLoadingDialog(message = "") {
                    isLoading = false
                }
            }
            if (isShowErrorDialog){
                showDialog(title = "Fail", message = errorMessage) {
                    isShowErrorDialog = false
                }
            }
            if (isShowSuccessDialog){
                showDialog(title = "Success", message = successMessage) {
                    isShowSuccessDialog = false
                    loginViewModel.resetState()
                    profileViewModel.setUpdatePasswordStateEmpty()
                    navController.navigate(Destinations.APP_MAIN_ROUTE) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    })
}