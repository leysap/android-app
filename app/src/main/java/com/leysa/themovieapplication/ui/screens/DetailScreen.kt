package com.leysa.themovieapplication.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.leysa.themovieapplication.constants.Preferences
import com.leysa.themovieapplication.ui.navigation.AppScreens
import com.leysa.themovieapplication.ui.theme.TheMovieApplicationTheme
import com.leysa.themovieapplication.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen (movieId: Int, appViewModel: AppViewModel, navController: NavController){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(Preferences.FILE, Context.MODE_PRIVATE)
    val apiKey = sharedPreferences.getString(Preferences.API_KEY, null)

    // Si la API Key está vacía, muestra un mensaje y redirige a WelcomeScreen
    if (apiKey.isNullOrEmpty()) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "API Key not found. Please enter it on the welcome screen.", Toast.LENGTH_LONG).show()
            navController.navigate(AppScreens.WelcomeScreen.route) {
                popUpTo(AppScreens.MainScreen.route) { inclusive = true }
            }
        }
    } else {
        // Si la API Key está presente, obtener detalles de la película seleccionada
        LaunchedEffect(movieId) {
            appViewModel.fetchMovieDetails(movieId, apiKey)
        }
    }

    val movieDetails = appViewModel.movieDetails.collectAsState().value

    TheMovieApplicationTheme (darkTheme = sharedPreferences.getBoolean(Preferences.APP_IN_DARK_THEME, false)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = movieDetails?.title ?: "Movie details") },
                    navigationIcon = {
                        // Botón de volver
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (movieDetails != null) {
                    // Mostrar los detalles de la película
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Mostrar el póster
                        Image(
                            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movieDetails.poster_path}"),
                            contentDescription = movieDetails.title,
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Mostrar el nombre de la película
                        Text(text = movieDetails.title, style = MaterialTheme.typography.headlineMedium)

                        Spacer(modifier = Modifier.height(8.dp))

                        // Mostrar la fecha de estreno
                        Text(text = "Release Date: ${movieDetails.release_date}")

                        Spacer(modifier = Modifier.height(8.dp))

                        // Mostrar el resumen
                        Text(text = "Overview: ${movieDetails.overview}")


                    }
                } else {
                    // Mostrar un mensaje de carga mientras se obtienen los detalles
                    Text(text = "Loading details...", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}