// Android helper to register Firebase Analytics with the common Analytics facade
package com.devphill.cocktails.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import android.os.Build

fun registerAndroidAnalytics(context: Context) {
    val firebase = FirebaseAnalytics.getInstance(context.applicationContext)
    // Capture device info once
    val deviceName = Build.MODEL ?: "unknown"
    val osVersion = Build.VERSION.RELEASE ?: "unknown"

    Analytics.setLogger { screenName, params ->
        try {
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Compose")
                putString("device_name", deviceName)
                putString("os_version", osVersion)
                params?.forEach { (k, v) -> putString(k, v) }
            }
            firebase.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        } catch (t: Throwable) {
            // Don't crash the app if analytics fails
            println("Analytics Android log error: ${t.message}")
        }
    }
}
