package com.devphill.cocktails.data.manager

import com.devphill.cocktails.data.platform.PushNotificationManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.domain.interactor.NotificationInteractor
import kotlinx.coroutines.delay

class FirstLaunchManagerImpl(
    private val userPreferencesManager: UserPreferencesManager,
    private val pushNotificationManager: PushNotificationManager,
    private val notificationInteractor: NotificationInteractor,
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
                val notification = pushNotificationManager.showWelcomeNotification()
                notificationInteractor.insertNotification(notification)
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
