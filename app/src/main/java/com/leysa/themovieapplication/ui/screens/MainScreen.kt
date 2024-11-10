package com.leysa.themovieapplication.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.leysa.themovieapplication.constants.Preferences
import com.leysa.themovieapplication.data.Movie
import com.leysa.themovieapplication.ui.navigation.AppScreens
import com.leysa.themovieapplication.ui.theme.TheMovieApplicationTheme
import com.leysa.themovieapplication.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(appViewModel: AppViewModel, navController: NavController) {
    var timeWindow by remember { mutableStateOf("week") } // Usamos remember para mantener el estado de timeWindow
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(Preferences.FILE, Context.MODE_PRIVATE)
    val apiKey = sharedPreferences.getString(Preferences.API_KEY, null)

    // Verifica si la API Key está disponible
    if (apiKey.isNullOrEmpty()) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "API Key not found. Please enter it on the welcome screen.", Toast.LENGTH_LONG).show()
            navController.navigate(AppScreens.WelcomeScreen.route) {
                popUpTo(AppScreens.MainScreen.route) { inclusive = true }
            }
        }
    } else {
        // Cuando la API Key y el timeWindow cambian, vuelve a hacer la petición
        LaunchedEffect(apiKey, timeWindow) {
            appViewModel.fetchTrendingMovies(apiKey, timeWindow)
        }
    }

    val movies = appViewModel.movies.collectAsState().value
    val isLoading = appViewModel.isLoading.collectAsState().value

    TheMovieApplicationTheme(darkTheme = sharedPreferences.getBoolean(Preferences.APP_IN_DARK_THEME, false)) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Trending movies of the $timeWindow") })
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Botones para seleccionar el timeWindow
                    Row {
                        Button(onClick = {
                            timeWindow = "day" // Cambiar a 'day' cuando se presiona el botón
                        }) {
                            Text("Daily")
                        }
                        Button(onClick = {
                            timeWindow = "week" // Cambiar a 'week' cuando se presiona el botón
                        }) {
                            Text("Weekly")
                        }
                    }

                    // Mostrar contenido basado en el estado
                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Loading...")
                            }
                        }
                        movies.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No trending movies available")
                            }
                        }
                        else -> {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(128.dp),
                                contentPadding = PaddingValues(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(movies, key = { it.id }) { movie ->
                                    MovieItem(movie, navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .clickable {
                navController.navigate("movie_details/${movie.id}") // Navegar a la pantalla de detalles
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
            contentDescription = movie.title,
            modifier = Modifier
                .height(180.dp)
                .width(120.dp)
        )
        Text(text = movie.title, modifier = Modifier.padding(top = 4.dp))
    }
}
