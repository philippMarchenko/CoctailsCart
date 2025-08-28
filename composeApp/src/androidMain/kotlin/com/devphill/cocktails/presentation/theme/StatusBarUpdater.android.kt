package com.devphill.cocktails.presentation.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

actual fun updateStatusBarAppearance(isLight: Boolean) {
    // This will be called from the composable context
    // We need to get the current activity to update the status bar
}

@Composable
fun rememberStatusBarController(): StatusBarController? {
    val context = LocalContext.current
    return remember(context) {
        if (context is Activity) {
            StatusBarController(context)
        } else {
            null
        }
    }
}
