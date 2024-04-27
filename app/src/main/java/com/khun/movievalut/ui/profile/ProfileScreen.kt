package com.khun.movievalut.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    var imageUri: Uri? by remember {
        mutableStateOf(null)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                imageUri = it
                val file = fileFromContentUri(context, it)
                profileViewModel.uploadeImage(file)
            }
        }

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
                            Log.d("State >>>", "ProfileState.Loading")
                            isShowLoading = true
                            showErrorDialog = false
                        }

                        is ProfileState.Success -> {
                            Log.d("State >>>", "ProfileState.Success")
                            isShowLoading = false
                            showErrorDialog = false
                            profile = it.profile

                            if(!it.profile.imageUrl.isNullOrBlank()) {
                                imageUri = Uri.parse(it.profile.imageUrl)
                            }

                        }

                        is ProfileState.Error -> {
                            Log.d("State >>>", "ProfileState.Error")
                            errorMessage = it.message
                            profileViewModel.setProfileStateEmpty()
                            isShowLoading = false
                            showErrorDialog = true
                        }

                        is ProfileState.Empty -> {
                            Log.d("State >>>", "ProfileState.Empty")
                        }
                    }
                }

                LaunchedEffect(key1 = Unit) {
                    profileViewModel.getUserProfile()
                }

                GlideImage(
                   // model = if (profile.imageUrl != "") profile.imageUrl else "https://t3.ftcdn.net/jpg/05/16/27/58/360_F_516275801_f3Fsp17x6HQK0xQgDQEELoTuERO4SsWV.jpg",
                    model = if (imageUri != null) imageUri else "https://t3.ftcdn.net/jpg/05/16/27/58/360_F_516275801_f3Fsp17x6HQK0xQgDQEELoTuERO4SsWV.jpg",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable {
                            launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    contentScale = ContentScale.Crop,
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

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "temp_image")
    inputStream?.let { input ->
        val outputStream = FileOutputStream(file)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024)
            var byteCount: Int
            while (input.read(buffer).also { byteCount = it } != -1) {
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
    return file
}

private fun fileFromContentUri(context: Context, contentUri: Uri): File {
    val fileExtension = getFileExtension(context, contentUri)
    val fileName = "temporary_file" + if (fileExtension != null) ".$fileExtension" else ""

    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()

    try {
        val oStream = FileOutputStream(tempFile)
        val inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let {
            copy(inputStream, oStream)
        }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}


