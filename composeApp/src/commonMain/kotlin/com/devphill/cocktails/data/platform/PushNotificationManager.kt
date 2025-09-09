package com.devphill.cocktails.data.platform

import com.devphill.cocktails.domain.model.Notification

/**
 * Platform-specific interface for managing push notifications.
 */
expect class PushNotificationManager {
    suspend fun showNotification(notification: Notification)

    suspend fun showWelcomeNotification(): Notification

    fun requestPermissions()

    fun isPermissionGranted(): Boolean
}
