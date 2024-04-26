package com.khun.movievalut.ui.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.khun.movievalut.Destinations
import com.khun.movievalut.data.model.Movie
import com.khun.movievalut.nav.BottomNavigationBar
import com.khun.movievalut.ui.movie.YoutubePlayer
import com.khun.movievalut.ui.util.savedMovies
import com.khun.movievalut.viewmodel.MovieViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    movieViewModel: MovieViewModel,
    navController: NavController
) {

    Scaffold(bottomBar = {
        BottomAppBar { BottomNavigationBar(navController = navController) }
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchScreenItem(movieViewModel = movieViewModel, navController = navController)
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenItem(
    movieViewModel: MovieViewModel,
    navController: NavController
) {
    var text by remember { mutableStateOf("") } // Query for SearchBar
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val searchHistory = remember { mutableStateListOf("") }
    var movies by remember {
        mutableStateOf(listOf<Movie>())
    }

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        SearchBar(modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                searchHistory.add(text)
                Log.d("Save Movies On Search", savedMovies.toString())
                movies = savedMovies.filter { movie -> movie.movieTitle.contains(text) }
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = "Search movie here")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        ) {
            searchHistory.forEach {
                if (it.isNotEmpty()) {
                    Row(modifier = Modifier.padding(all = 14.dp)) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = it, modifier = Modifier.clickable {
                            Log.d("Save Movies", savedMovies.toString())
                            movies = savedMovies.filter { movie ->
                                movie.movieTitle.contains(text)
                            }
                            active = false
                        })
                    }
                }
            }

            Divider()
            Text(
                modifier = Modifier
                    .padding(all = 14.dp)
                    .fillMaxWidth()
                    .clickable {
                        searchHistory.clear()
                    },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = "clear all history"
            )
        }

        LazyColumn {
            items(movies) {
                MovieItem(movie = it) {
                    movieViewModel.setMovieInfo(movie = it)
                    movieViewModel.setMovieStateEmpty()
                    navController.navigate(Destinations.MOVIE_DETAIL_ROUTE)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            onMovieClick(movie)
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster}",
                contentDescription = "PosterPath",
                Modifier
                    .size(100.dp)
                    .padding(10.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.Center) {
                Text(text = movie.movieTitle)
                Text(text = movie.releaseDate)
                Text(text = movie.rating.toString())
            }
        }

    }
}

@Composable
fun showYoutubeVideo(url: String) {
    YoutubePlayer(
        youtubeVideoId = url,
        lifecycleOwner = LocalLifecycleOwner.current
    )
}


