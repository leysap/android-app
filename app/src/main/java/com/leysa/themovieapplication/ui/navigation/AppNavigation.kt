package com.leysa.themovieapplication.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leysa.themovieapplication.ui.screens.DetailScreen
import com.leysa.themovieapplication.ui.screens.MainScreen
import com.leysa.themovieapplication.ui.screens.WelcomeScreen
import com.leysa.themovieapplication.ui.viewmodels.AppViewModel

/**
 * Función para definir la navegación de la app.
 * */

@Composable
fun AppNavigation() {

    val appViewModel = AppViewModel() // Se crea una instancia de AppViewModel para manejar el estado de la app y compartirlo entre pantallas
    val navController = rememberNavController() // Crea un objeto NavController para controlar la navegación entre las pantallas

    // Contenedor de las diferentes pantallas de la app
    NavHost(
        navController = navController,
        startDestination = AppScreens.WelcomeScreen.route, // Pantalla inicial
        enterTransition = { fadeIn() }, // Animación de entrada entre las pantallas
        exitTransition = { fadeOut() }, // Animación de salida entre las pantallas
    ) {
        composable(route = AppScreens.MainScreen.route) {
            MainScreen(appViewModel, navController)
        }

        composable(route = AppScreens.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }

        composable("movie_details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: return@composable
            DetailScreen(movieId = movieId, appViewModel = appViewModel, navController = navController)
        }

    }

}
