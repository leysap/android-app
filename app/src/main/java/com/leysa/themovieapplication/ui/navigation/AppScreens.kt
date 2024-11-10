package com.leysa.themovieapplication.ui.navigation

sealed class AppScreens(val route: String) {

    data object MainScreen : AppScreens("main_screen")
    data object DetailScreen : AppScreens("movie_details")
    data object WelcomeScreen : AppScreens("welcome_screen")
}