package com.devphill.cocktails.presentation.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

@Composable
actual fun updateStatusBarAppearance(isLight: Boolean) {
    val context = LocalContext.current

    LaunchedEffect(isLight) {
        if (context is Activity) {
            val window = context.window
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            // Set status bar appearance
            insetsController.isAppearanceLightStatusBars = isLight
        }
    }
}
