package com.khun.movievalut.ui.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khun.movievalut.R
import com.khun.movievalut.data.model.Profile
import com.khun.movievalut.data.model.RegisterUserResponse
import com.khun.movievalut.data.model.User
import com.khun.movievalut.deligation.RegisterUserState
import com.khun.movievalut.ui.login.ConfirmPasswordState
import com.khun.movievalut.ui.login.Email
import com.khun.movievalut.ui.login.EmailState
import com.khun.movievalut.ui.login.Password
import com.khun.movievalut.ui.login.PasswordState
import com.khun.movievalut.ui.theme.stronglyDeemphasizedAlpha
import com.khun.movievalut.ui.util.ROLE_USER
import com.khun.movievalut.ui.util.SecretKey
import com.khun.movievalut.ui.util.encryptPassword
import com.khun.movievalut.ui.util.showDialog
import com.khun.movievalut.ui.util.showLoadingDialog
import com.khun.movievalut.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            RegisterTopAppBar(
                topAppBarText = stringResource(id = R.string.create_account),
                onNavUp = {
                    registerViewModel.setRegisterUserStateEmpty()
                    navController.popBackStack()
                },
            )
        },
        content = { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                RegisterContent(
                    registerViewModel = registerViewModel
                ) {
                    registerViewModel.setRegisterUserStateEmpty()
                    navController.popBackStack()
                }
            }
        }
    )

}

@Composable
fun RegisterContent(
    registerViewModel: RegisterViewModel,
    onRegisterSubmitted: (RegisterUserResponse) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var fullName by remember {
            mutableStateOf("")
        }
        var contactNo by remember {
            mutableStateOf("")
        }
        val passwordFocusRequest = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }
        val emailState = remember { EmailState("") }
        val passwordState = remember { PasswordState() }
        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }

        var title by remember {
            mutableStateOf("")
        }
        var message by remember {
            mutableStateOf("")
        }
        var isShowLoading by remember {
            mutableStateOf(false)
        }
        var showSuccessDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }

        var user = remember {
            RegisterUserResponse()
        }


        val onSubmit = {
            val profile = Profile(0, fullName, contactNo, "")
            val email = emailState.text
            val password = encryptPassword(SecretKey, passwordState.text)
            registerViewModel.registerUser(
                User(0, email, password, profile, ROLE_USER)
            )
        }

        registerViewModel.registerUserState.observeForever {
            when (it) {
                is RegisterUserState.Loading -> {
                    isShowLoading = true
                }

                is RegisterUserState.Success -> {
                    user = it.registerUserResponse
                    title = "Registration Success"
                    message = "Congratulation, Your account is successfully created."
                    isShowLoading = false
                    showSuccessDialog = true
                    showErrorDialog = false
                }

                is RegisterUserState.Error -> {
                    registerViewModel.setRegisterUserStateEmpty()
                    title = "Registration Fail"
                    message = "Fail: ${it.message}"
                    isShowLoading = false
                    showSuccessDialog = false
                    showErrorDialog = true
                }

                is RegisterUserState.Empty -> {
                }
            }
        }

        if (showSuccessDialog) {
            showDialog(title = title, message = message) {
                showSuccessDialog = false
                onRegisterSubmitted(user)
            }
        }
        if (showErrorDialog) {
            showDialog(title = title, message = message) {
                registerViewModel.setRegisterUserStateEmpty()
                showErrorDialog = false
            }
        }
        if (isShowLoading) {
            showLoadingDialog(message = "") {
                isShowLoading = false
            }
        }


        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
            },
            label = {
                Text(
                    text = stringResource(R.string.full_name),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contactNo,
            onValueChange = {
                contactNo = it
            },
            label = {
                Text(
                    text = stringResource(R.string.mobile_number),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        Email(emailState, onImeAction = { passwordFocusRequest.requestFocus() })

        Spacer(modifier = Modifier.height(8.dp))


        Password(
            label = stringResource(R.string.password),
            passwordState = passwordState,
            imeAction = ImeAction.Next,
            onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(passwordFocusRequest)
        )


        Spacer(modifier = Modifier.height(8.dp))


        Password(
            label = stringResource(R.string.confirm_password),
            passwordState = confirmPasswordState,
            onImeAction = { onSubmit() },
            modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.terms_and_conditions),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSubmit()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid &&
                    passwordState.isValid && confirmPasswordState.isValid
        ) {
            Text(text = stringResource(R.string.create_account))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class) // CenterAlignedTopAppBar is experimental in m3
@Composable
fun RegisterTopAppBar(
    topAppBarText: String,
    onNavUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = topAppBarText,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        // We need to balance the navigation icon, so we add a spacer.
        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },
    )
}
