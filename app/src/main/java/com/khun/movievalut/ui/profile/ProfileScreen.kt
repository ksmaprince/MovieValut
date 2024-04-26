package com.khun.movievalut.ui.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.khun.movievalut.Destinations.APP_MAIN_ROUTE
import com.khun.movievalut.Destinations.PASSWORD_EDIT_ROUTE
import com.khun.movievalut.Destinations.PROFILE_EDIT_ROUTE
import com.khun.movievalut.R
import com.khun.movievalut.data.model.Profile
import com.khun.movievalut.deligation.ProfileState
import com.khun.movievalut.nav.BottomNavigationBar
import com.khun.movievalut.ui.util.showDialog
import com.khun.movievalut.ui.util.showLoadingDialog
import com.khun.movievalut.ui.util.userEmail
import com.khun.movievalut.viewmodel.LoginViewModel
import com.khun.movievalut.viewmodel.ProfileViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {

    Scaffold(bottomBar = {
        BottomAppBar { BottomNavigationBar(navController = navController) }
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                var profile by remember {
                    mutableStateOf(Profile())
                }

                var showErrorDialog by remember {
                    mutableStateOf(false)
                }
                var errorMessage by remember {
                    mutableStateOf("")
                }
                var isShowLoading by remember {
                    mutableStateOf(false)
                }

                profileViewModel.profile.observeForever {
                    profile = it
                }

                profileViewModel.profileState.observeForever {
                    when (it) {
                        is ProfileState.Loading -> {
                            isShowLoading = true
                            showErrorDialog = false
                        }

                        is ProfileState.Success -> {
                            isShowLoading = false
                            showErrorDialog = false
                            profile = it.profile
                        }

                        is ProfileState.Error -> {
                            errorMessage = it.message
                            profileViewModel.setProfileStateEmpty()
                            isShowLoading = false
                            showErrorDialog = true
                        }

                        is ProfileState.Empty -> {
                        }
                    }
                }

                LaunchedEffect(key1 = Unit) {
                    profileViewModel.getUserProfile()
                }

                GlideImage(
                    model = if (profile.imageUrl != "") profile.imageUrl else "https://t3.ftcdn.net/jpg/05/16/27/58/360_F_516275801_f3Fsp17x6HQK0xQgDQEELoTuERO4SsWV.jpg",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentDescription = "Image"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = profile.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = profile.contactNo,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userEmail.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = {
                        profileViewModel.setProfileStateEmpty()
                        profileViewModel.setProfile(profile)
                        navController.navigate(PROFILE_EDIT_ROUTE)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.edit_profile))
                }

                TextButton(
                    onClick = {
                        profileViewModel.setProfileStateEmpty()
                        navController.navigate(PASSWORD_EDIT_ROUTE)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.change_password))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        loginViewModel.resetState()
                        profileViewModel.setProfileStateEmpty()
                        navController.navigate(APP_MAIN_ROUTE) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = true
                ) {
                    Text(
                        text = stringResource(R.string.logout),
                    )
                }

                if (isShowLoading) {
                    showLoadingDialog(message = "") {
                        isShowLoading = false
                    }
                }

                if (showErrorDialog) {
                    showDialog(title = "Fail", message = errorMessage) {
                        showErrorDialog = false
                    }
                }
            }
        }
    }
}

