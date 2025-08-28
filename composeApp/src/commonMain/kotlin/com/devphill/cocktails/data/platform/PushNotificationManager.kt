package com.devphill.cocktails.data.platform

import com.devphill.cocktails.data.model.Notification
import com.devphill.cocktails.data.model.NotificationType
/**
 * Platform-specific interface for managing push notifications.
 */
expect class PushNotificationManager{
    suspend fun showNotification(notification: Notification)
    suspend fun showWelcomeNotification()
    suspend fun scheduleNotification(notification: Notification, delayMillis: Long = 0)
    fun requestPermissions()
    fun isPermissionGranted(): Boolean
}
