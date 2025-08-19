package com.devphill.cocktails.ui.theme

import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

actual class StatusBarController(private val activity: Activity) {
    actual fun setStatusBarAppearance(isLight: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = isLight
    }
}
