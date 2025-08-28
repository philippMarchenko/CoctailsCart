package com.devphill.cocktails.data.platform

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.devphill.cocktails.MainActivity
import org.koin.core.component.KoinComponent

actual class NotificationPermissionManager : KoinComponent {

    // Store reference to MainActivity for permission requests
    companion object {
        private var mainActivity: MainActivity? = null

        fun setMainActivity(activity: MainActivity) {
            mainActivity = activity
        }

        fun clearMainActivity() {
            mainActivity = null
        }
    }

    actual fun requestPermissionIfNeeded() {
        mainActivity?.requestNotificationPermissionIfNeeded()
    }

    actual fun isPermissionGranted(): Boolean {
        val activity = mainActivity ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Pre-Android 13 doesn't require explicit notification permission
        }
    }
}
