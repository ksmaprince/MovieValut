package com.khun.movievalut.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.khun.movievalut.Destinations.MOVIE_DETAIL_ROUTE
import com.khun.movievalut.data.model.Movie
import com.khun.movievalut.deligation.MovieState
import com.khun.movievalut.nav.BottomNavigationBar
import com.khun.movievalut.ui.util.showLoadingDialog
import com.khun.movievalut.ui.util.showToastMessage
import com.khun.movievalut.viewmodel.MovieViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(movieViewModel: MovieViewModel, navController: NavController) {

    Scaffold(bottomBar = {
        BottomAppBar { BottomNavigationBar(navController = navController) }
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            HomeScreenItem(movieViewModel = movieViewModel, navController = navController)
        }
    }
}

@Composable
fun HomeScreenItem(movieViewModel: MovieViewModel, navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var firstBackPressed by remember {
        mutableStateOf(false)
    }

    var movies by remember {
        mutableStateOf(listOf<Movie>())
    }
    var isShowLoading by remember { mutableStateOf(false) }

    if (isShowLoading) {
        showLoadingDialog(message = "Fetching data ...") {
            isShowLoading = false
        }
    }

    movieViewModel.movieState.observeForever {
        when (it) {
            is MovieState.Loading -> {
                isShowLoading = true
            }

            is MovieState.Success -> {
                isShowLoading = false
                movies = it.movies
            }

            is MovieState.Error -> {
                isShowLoading = false
                movieViewModel.setMovieStateEmpty()
                showToastMessage(context = context, message = it.message)
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }

            is MovieState.Empty -> {
                isShowLoading = false
            }
        }
    }
    LaunchedEffect(Unit) {
        movieViewModel.fetchAllMovie()
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                Modifier.padding(4.dp)
            ) {
                items(movies) {
                    MovieItem(movie = it, viewModel = movieViewModel, navController)
                }
            }
        }
    }

    BackHandler {
        if (firstBackPressed) {
            val activity = context as Activity
            activity.finish()
        } else {
            firstBackPressed = true
            showToastMessage(context = context, message = "Press again to exit")
            coroutineScope.launch {
                delay(2000L)
                firstBackPressed = false
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(movie: Movie, viewModel: MovieViewModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        onClick = {
            viewModel.setMovieInfo(movie = movie)
            viewModel.setMovieStateEmpty()
            navController.navigate(MOVIE_DETAIL_ROUTE)
        }
    ) {
        GlideImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster}",
            contentDescription = "Poster",
            Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(4.dp)
        ) {
            Text(
                text = movie.movieTitle, style = MaterialTheme.typography.titleMedium
            )
            Text(text = movie.releaseDate)
        }
    }
}
