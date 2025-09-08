package com.devphill.cocktails.data.platform

import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType
import kotlinx.datetime.Clock
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

actual class PushNotificationManager {
    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    actual suspend fun showNotification(notification: Notification) {
        if (!isPermissionGranted()) {
            requestPermissions()
            return
        }

        val content =
            UNMutableNotificationContent().apply {
                setTitle(notification.title)
                setBody(notification.message)
                setSound(UNNotificationSound.defaultSound())
                setCategoryIdentifier("COCKTAIL_NOTIFICATION")

                // Add user info for handling tap
                val userInfo =
                    mapOf<Any?, Any?>(
                        "notification_id" to notification.id,
                        "cocktail_id" to (notification.cocktailId ?: ""),
                    )
                setUserInfo(userInfo)
            }

        val request =
            UNNotificationRequest.requestWithIdentifier(
                identifier = notification.id,
                content = content,
                trigger = null, // Show immediately
            )

        notificationCenter.addNotificationRequest(request) { error ->
            error?.let {
                println("Failed to show notification: ${error.localizedDescription}")
            }
        }
    }

    actual suspend fun showWelcomeNotification(): Notification {
        val welcomeNotification =
            Notification(
                id = "welcome_${Clock.System.now().toEpochMilliseconds()}",
                title = "Welcome to CocktailsCraft! 🍹",
                message = "Discover amazing cocktail recipes and start your mixology journey today!",
                type = NotificationType.SYSTEM_MESSAGE,
                timestamp = Clock.System.now().toEpochMilliseconds().toString(),
                isRead = false,
            )

        showNotification(welcomeNotification)
        return welcomeNotification
    }

    actual fun requestPermissions() {
        notificationCenter.requestAuthorizationWithOptions(
            options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
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
