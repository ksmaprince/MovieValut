package com.khun.movievalut

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khun.movievalut.Destinations.LOGIN_ROUTE
import com.khun.movievalut.Destinations.MOVIE_HOME_ROUTE
import com.khun.movievalut.Destinations.REGISTER_ROUTE
import com.khun.movievalut.nav.NavItem
import com.khun.movievalut.ui.favourites.FavouriteMoviesScreen
import com.khun.movievalut.ui.home.HomeScreen
import com.khun.movievalut.ui.login.LoginScreen
import com.khun.movievalut.ui.movie.MovieDetailScreen
import com.khun.movievalut.ui.profile.ChangePasswordScreen
import com.khun.movievalut.ui.profile.EditProfileScreen
import com.khun.movievalut.ui.profile.ProfileScreen
import com.khun.movievalut.ui.register.RegisterScreen
import com.khun.movievalut.ui.search.SearchScreen
import com.khun.movievalut.viewmodel.FavouriteMovieViewModel
import com.khun.movievalut.viewmodel.LoginViewModel
import com.khun.movievalut.viewmodel.MovieViewModel
import com.khun.movievalut.viewmodel.ProfileViewModel
import com.khun.movievalut.viewmodel.RegisterViewModel

object Destinations {
    const val APP_MAIN_ROUTE = "main"
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val MOVIE_HOME_ROUTE = "home"
    const val MOVIE_DETAIL_ROUTE = "moviedetail"
    const val PROFILE_EDIT_ROUTE = "editprofile"
    const val PASSWORD_EDIT_ROUTE = "passwordedit"
}

@Composable
fun MovieVaultNavHost(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    profileViewModel: ProfileViewModel,
    movieViewModel: MovieViewModel,
    favouriteMovieViewModel: FavouriteMovieViewModel,
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = LOGIN_ROUTE) {
        composable(LOGIN_ROUTE) {
            LoginScreen(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(REGISTER_ROUTE) {
            RegisterScreen(
                registerViewModel = registerViewModel, navController = navController
            )
        }

        composable(NavItem.Home.path) { HomeScreen(movieViewModel = movieViewModel, navController) }
        composable(NavItem.Search.path) {
            SearchScreen(
                movieViewModel = movieViewModel,
                navController
            )
        }
        composable(NavItem.List.path) {
            FavouriteMoviesScreen(
                favouriteMovieViewModel = favouriteMovieViewModel,
                navController = navController
            )
        }
        composable(NavItem.Profile.path) {
            ProfileScreen(
                loginViewModel = loginViewModel,
                profileViewModel = profileViewModel,
                navController = navController
            )
        }
        composable(Destinations.MOVIE_DETAIL_ROUTE) {
            MovieDetailScreen(
                movieViewModel = movieViewModel,
                favouriteMovieViewModel = favouriteMovieViewModel,
                navController = navController
            )
        }
        composable(Destinations.PROFILE_EDIT_ROUTE) {
            EditProfileScreen(profileViewModel = profileViewModel, navController = navController)
        }
        composable(Destinations.APP_MAIN_ROUTE) {
            MovieVaultNavHost(
                loginViewModel = loginViewModel,
                registerViewModel = registerViewModel,
                profileViewModel = profileViewModel,
                movieViewModel = movieViewModel,
                favouriteMovieViewModel = favouriteMovieViewModel
            )
        }

        composable(Destinations.PASSWORD_EDIT_ROUTE) {
            ChangePasswordScreen(
                loginViewModel = loginViewModel,
                profileViewModel = profileViewModel,
                navController = navController
            )
        }

        composable(MOVIE_HOME_ROUTE) {
            HomeScreen(movieViewModel = movieViewModel, navController)
        }

    }
}


