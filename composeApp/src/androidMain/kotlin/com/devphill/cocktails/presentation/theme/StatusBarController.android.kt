package com.devphill.cocktails.presentation.theme

import android.app.Activity
import androidx.core.view.WindowCompat

actual class StatusBarController(private val activity: Activity) {
    actual fun setStatusBarAppearance(isLight: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = isLight
    }
}
