package com.leysa.themovieapplication.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.leysa.themovieapplication.constants.Preferences
import com.leysa.themovieapplication.ui.navigation.AppScreens
import com.leysa.themovieapplication.ui.theme.TheMovieApplicationTheme
import com.leysa.themovieapplication.ui.utils.SetSystemBarsVisibility
import com.leysa.themovieapplication.R


@Composable
fun WelcomeScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(Preferences.FILE, Context.MODE_PRIVATE)

    var areSystemBarsVisible by rememberSaveable { mutableStateOf(false) }
    SetSystemBarsVisibility(visible = areSystemBarsVisible)

    var isDarkTheme by rememberSaveable { mutableStateOf(sharedPreferences.getBoolean(Preferences.APP_IN_DARK_THEME, false)) }
    var apiKey by rememberSaveable { mutableStateOf(sharedPreferences.getString(Preferences.API_KEY, "") ?: "") }
    var apiKeyInput by rememberSaveable { mutableStateOf("") }
    var showApiKeyInput by rememberSaveable { mutableStateOf(apiKey.isEmpty()) }

    TheMovieApplicationTheme(darkTheme = isDarkTheme) {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.fondo)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Movies App",
                        color = Color.White,
                        fontSize = 40.sp,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .width(220.dp),
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(4f, 4f),
                                blurRadius = 8f
                            )
                        )
                    )

                    if (showApiKeyInput) {
                        TextField(
                            value = apiKeyInput,
                            onValueChange = { apiKeyInput = it },
                            label = { Text("Enter API Key") },
                            modifier = Modifier.padding(top = 15.dp)
                        )
                        Button(
                            onClick = {
                                if (apiKeyInput.isNotEmpty()) {
                                    sharedPreferences.edit().putString(Preferences.API_KEY, apiKeyInput).apply()
                                    apiKey = apiKeyInput
                                    showApiKeyInput = false
                                }
                            },
                            modifier = Modifier.padding(top = 15.dp)
                        ) {
                            Text("Save API Key")
                        }
                    } else {
                        Row(
                            modifier = Modifier.padding(top = 15.dp)
                        ) {
                            Button(
                                onClick = {
                                    isDarkTheme = false
                                    sharedPreferences.edit().putBoolean(Preferences.APP_IN_DARK_THEME, isDarkTheme).apply()
                                },colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFBBDEFB), // Light blue background
                                    contentColor = Color.Black // Black icon color
                                )
                            ) {
                                Icon(Icons.Filled.LightMode, null)
                            }
                            Button(
                                onClick = {
                                    isDarkTheme = true
                                    sharedPreferences.edit().putBoolean(Preferences.APP_IN_DARK_THEME, isDarkTheme).apply()
                                },colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF303F9F), // Dark blue background
                                    contentColor = Color.White // White icon color
                                ),
                                modifier = Modifier.padding(start = 15.dp)
                            ) {
                                Icon(Icons.Filled.DarkMode, null)
                            }
                        }
                        Button(
                            onClick = {
                                navController.navigate(
                                    route = AppScreens.MainScreen.route,
                                    navOptions = NavOptions.Builder()
                                        .setPopUpTo(navController.graph.startDestinationId, true)
                                        .build()
                                )
                                areSystemBarsVisible = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDarkTheme) Color(0xFF303F9F) else Color(0xFFBBDEFB), // Dark blue for dark theme, light blue for light theme
                                contentColor = if (isDarkTheme) Color.White else Color.Black // White text in dark theme, black in light theme
                            ),
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .width(220.dp)
                        ) {
                            Text(text = "Get Started")
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(rememberNavController())
}
