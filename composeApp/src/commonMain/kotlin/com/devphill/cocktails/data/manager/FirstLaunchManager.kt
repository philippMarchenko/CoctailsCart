package com.devphill.cocktails.data.manager

import com.devphill.cocktails.data.platform.PushNotificationManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.delay

interface FirstLaunchManager {
    suspend fun handleFirstLaunch() // Back to original name since it only handles first launch now
    suspend fun isFirstLaunch(): Boolean
    suspend fun markAsLaunched()
}

class FirstLaunchManagerImpl(
    private val userPreferencesManager: UserPreferencesManager,
    private val pushNotificationManager: PushNotificationManager
) : FirstLaunchManager {

    companion object {
        private const val FIRST_LAUNCH_KEY = "is_first_launch"
        private const val WELCOME_DELAY_MS = 3000L // 3 seconds delay
    }

    override suspend fun handleFirstLaunch() {
        // Only show welcome notification on first launch
        if (isFirstLaunch()) {
            // Wait a bit for the user to see the app
            delay(WELCOME_DELAY_MS)

            // Show welcome push notification only if permissions are granted
            if (pushNotificationManager.isPermissionGranted()) {
                pushNotificationManager.showWelcomeNotification()
            }

            // Mark as launched regardless of notification permission status
            markAsLaunched()
        }
    }

    override suspend fun isFirstLaunch(): Boolean {
        return userPreferencesManager.getBoolean(FIRST_LAUNCH_KEY, true)
    }

    override suspend fun markAsLaunched() {
        userPreferencesManager.putBoolean(FIRST_LAUNCH_KEY, false)
    }
}
