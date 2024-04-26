package com.khun.movievalut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.khun.movievalut.ui.theme.MovieValutTheme
import com.khun.movievalut.viewmodel.FavouriteMovieViewModel
import com.khun.movievalut.viewmodel.LoginViewModel
import com.khun.movievalut.viewmodel.MovieViewModel
import com.khun.movievalut.viewmodel.ProfileViewModel
import com.khun.movievalut.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var registerViewModel: RegisterViewModel

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var movieViewModel: MovieViewModel

    @Inject
    lateinit var favouriteMovieViewModel: FavouriteMovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieValutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieVaultNavHost(
                        loginViewModel = loginViewModel,
                        registerViewModel = registerViewModel,
                        profileViewModel = profileViewModel,
                        movieViewModel = movieViewModel,
                        favouriteMovieViewModel = favouriteMovieViewModel
                    )
                }
            }
        }
    }
}
