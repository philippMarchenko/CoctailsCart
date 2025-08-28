package com.devphill.cocktails.data.platform

import com.devphill.cocktails.data.model.Notification
import com.devphill.cocktails.data.model.NotificationType
import kotlinx.coroutines.delay
import platform.UserNotifications.*
import platform.Foundation.*

actual class PushNotificationManager {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    actual suspend fun showNotification(notification: Notification) {
        if (!isPermissionGranted()) {
            requestPermissions()
            return
        }

        val content = UNMutableNotificationContent().apply {
            setTitle(notification.title)
            setBody(notification.message)
            setSound(UNNotificationSound.defaultSound())
            setCategoryIdentifier("COCKTAIL_NOTIFICATION")

            // Add user info for handling tap
            val userInfo = NSMutableDictionary().apply {
                setObject(notification.id, forKey = "notification_id")
                notification.cocktailId?.let {
                    setObject(it, forKey = "cocktail_id")
                }
            }
            setUserInfo(userInfo)
        }

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = notification.id,
            content = content,
            trigger = null // Show immediately
        )

        notificationCenter.addNotificationRequest(request) { error ->
            error?.let {
                println("Failed to show notification: ${error.localizedDescription}")
            }
        }
    }

    actual suspend fun showWelcomeNotification() {
        val welcomeNotification = Notification(
            id = "welcome_${NSDate().timeIntervalSince1970.toLong()}",
            title = "Welcome to CocktailsCraft! 🍹",
            message = "Discover amazing cocktail recipes and start your mixology journey today!",
            type = NotificationType.SYSTEM_MESSAGE,
            timestamp = NSDate().timeIntervalSince1970.toLong().toString(),
            isRead = false
        )

        showNotification(welcomeNotification)
    }

    actual suspend fun scheduleNotification(notification: Notification, delayMillis: Long) {
        if (delayMillis > 0) {
            delay(delayMillis)
        }
        showNotification(notification)
    }

    actual fun requestPermissions() {
        notificationCenter.requestAuthorizationWithOptions(
            options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        ) { granted, error ->
            if (granted) {
                println("Notification permission granted")
            } else {
                println("Notification permission denied: ${error?.localizedDescription}")
            }
        }
    }

    actual fun isPermissionGranted(): Boolean {
        // This is a simplified check - in real implementation you'd need to check async
        // For now, we'll assume permission is needed and request it
        return false
    }
}
