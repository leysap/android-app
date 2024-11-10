package com.leysa.themovieapplication.ui.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


/**
 * Cambia la visibilidad de las barras del sistema.
 * Ejemplo de uso:
 * ```
 * var areSystemBarsVisible  by rememberSaveable { mutableStateOf(false) }
 * SetSystemBarsVisibility(visible = areSystemBarsVisible)
 * ```
 * */
@Composable
fun SetSystemBarsVisibility(visible: Boolean) {

    val view = LocalView.current
    val window = (view.context as Activity).window
    val insetsController = WindowCompat.getInsetsController(window, view)
    if (!view.isInEditMode) {
        if (!visible) {
            insetsController.apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            insetsController.apply { show(WindowInsetsCompat.Type.systemBars()) }
        }
    }

}