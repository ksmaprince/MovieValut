package com.khun.movievalut.ui.profile

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khun.movievalut.R
import com.khun.movievalut.data.model.Profile
import com.khun.movievalut.deligation.EditProfileState
import com.khun.movievalut.ui.util.showDialog
import com.khun.movievalut.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel, navController: NavController
) {
    Scaffold(topBar = {
        EditProfileTopAppBar(
            topAppBarText = stringResource(id = R.string.edit_profile),
            onNavUp = {
                profileViewModel.setEditProfileStateEmpty()
                navController.popBackStack()
                      },
        )
    }, content = { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            EditProfileContent(profileViewModel = profileViewModel, onSubmitted = {
                profileViewModel.setEditProfileStateEmpty()
                profileViewModel.setProfile(profile = it)
                navController.popBackStack()
            })
        }
    })
}

@Composable
fun EditProfileContent(
    profileViewModel: ProfileViewModel, onSubmitted: (Profile) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val profile = profileViewModel.profile.value

        var fullName by remember {
            mutableStateOf(profile!!.fullName)
        }
        var contactNo by remember {
            mutableStateOf(profile!!.contactNo)
        }

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


        profileViewModel.editProfileState.observeForever {
            when (it) {
                is EditProfileState.Loading -> {
                    isShowLoading = true
                    showSuccessDialog = false
                    showErrorDialog = false
                }

                is EditProfileState.Success -> {
                    title = "Success"
                    message = "Congratulation, You have successfully updated your profile."
                    Log.d("DIALOG ++>>>", "State Changes")
                    isShowLoading = false
                    showSuccessDialog = true
                    showErrorDialog = false
                }

                is EditProfileState.Error -> {
                    title = "Fail"
                    message = "Update Fail: ${it.message}"
                    profileViewModel.setEditProfileStateEmpty()
                    isShowLoading = false
                    showSuccessDialog = false
                    showErrorDialog = true
                }

                is EditProfileState.Empty -> {

                }
            }
        }

        OutlinedTextField(value = fullName,
            onValueChange = {
                fullName = it
            },
            label = {
                Text(
                    text = stringResource(R.string.full_name),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = contactNo,
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
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                profileViewModel.updateUserProfile(
                    Profile(
                        profile!!.profileId, fullName, contactNo, profile.imageUrl
                    )
                )
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Profile")
        }

        if (showSuccessDialog) {
            showDialog(title = title, message = message) {
                showSuccessDialog = false
                onSubmitted(profile!!)
            }
        }
        if (showErrorDialog) {
            showDialog(title = title, message = message) {
                showErrorDialog = false
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class) // CenterAlignedTopAppBar is experimental in m3
@Composable
fun EditProfileTopAppBar(
    topAppBarText: String, onNavUp: () -> Unit
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
