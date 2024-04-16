package com.khun.movievalut

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khun.movievalut.Destinations.LOGIN_ROUTE
import com.khun.movievalut.Destinations.MOVIE_DETAIL_ROUTE
import com.khun.movievalut.Destinations.MOVIE_HOME_ROUTE
import com.khun.movievalut.Destinations.REGISTER_ROUTE
import com.khun.movievalut.nav.MainScreen
import com.khun.movievalut.ui.login.LoginScreen
import com.khun.movievalut.ui.register.RegisterScreen
import com.khun.movievalut.viewmodel.UserViewModel

object Destinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val MOVIE_HOME_ROUTE = "home"
    const val MOVIE_DETAIL_ROUTE = "moviedetail"

}

@Composable
fun MovieVaultNavHost(
    userViewModel: UserViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = LOGIN_ROUTE) {
        composable(LOGIN_ROUTE) {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSubmitted = { _, _ ->
                    navController.navigate(MOVIE_HOME_ROUTE)
                },
                onNewUser = {
                    navController.navigate(REGISTER_ROUTE)
                }
            )
        }
        composable(REGISTER_ROUTE) {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegisterSubmitted = { _, _ -> }
            ) { navController.popBackStack() }
        }

        composable(MOVIE_HOME_ROUTE) {
            MainScreen()
        }
        composable(MOVIE_DETAIL_ROUTE) {

        }

    }
}


