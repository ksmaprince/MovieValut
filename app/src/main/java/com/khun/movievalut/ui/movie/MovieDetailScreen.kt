package com.khun.movievalut.ui.movie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.khun.movievalut.deligation.FavouriteMovieState
import com.khun.movievalut.ui.register.RegisterTopAppBar
import com.khun.movievalut.ui.util.showDialog
import com.khun.movievalut.viewmodel.FavouriteMovieViewModel
import com.khun.movievalut.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieViewModel: MovieViewModel,
    favouriteMovieViewModel: FavouriteMovieViewModel,
    navController: NavController
) {
    val movieInfo = movieViewModel.movie.value

    movieInfo?.let {
        Scaffold(
            topBar = {
                RegisterTopAppBar(
                    topAppBarText = movieInfo.movieTitle,
                    onNavUp = {
                        favouriteMovieViewModel.setFavouriteMovieStateEmpty()
                        navController.popBackStack()
                    },
                )
            },
            content = { contentPadding ->
                Column(
                    Modifier
                        .padding(contentPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    var showSuccessDialog by remember {
                        mutableStateOf(false)
                    }
                    var showErrorDialog by remember {
                        mutableStateOf(false)
                    }
                    var errorMessage by remember {
                        mutableStateOf("")
                    }

                    favouriteMovieViewModel.favouriteMovieState.observeForever {
                        when (it) {
                            is FavouriteMovieState.Loading -> {

                            }

                            is FavouriteMovieState.addFavouriteSuccess -> {
                                showSuccessDialog = true
                            }

                            is FavouriteMovieState.Error -> {
                                errorMessage = it.message
                                showErrorDialog = true
                            }

                            is FavouriteMovieState.Empty -> {

                            }

                            else -> {}
                        }
                    }


//                GlideImage(
//                    model = "https://image.tmdb.org/t/p/w500${movieInfo.poster}",
//                    contentDescription = "PosterPath",
//                    Modifier.fillMaxWidth(),
//                    contentScale = ContentScale.Crop
//                )
                    YoutubePlayer(
                        youtubeVideoId = movieInfo.trailer,
                        lifecycleOwner = LocalLifecycleOwner.current
                    )
                    Text(
                        text = movieInfo.movieTitle,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Rating: ${movieInfo.rating} Release Date: ${movieInfo.releaseDate}")
                    }
                    Text(
                        text = movieInfo.overview,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = {
                            favouriteMovieViewModel.addFavoriteMovie(movieId = movieInfo.movieId)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Add To Favourite")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (showSuccessDialog) {
                        showDialog(title = "Success", message = "Add Favourite Success") {
                            showSuccessDialog = false
                        }
                    }

                    if (showErrorDialog) {
                        showDialog(title = "Fail", message = errorMessage) {
                            showErrorDialog = false
                        }
                    }


                }
            }
        )

    }

}