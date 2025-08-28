package com.devphill.cocktails.data.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devphill.cocktails.MainActivity
import com.devphill.cocktails.data.model.Notification
import com.devphill.cocktails.data.model.NotificationType
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class PushNotificationManager : KoinComponent {

    private val context: Context by inject()
    private val channelId = "cocktails_craft_notifications"
    private val channelName = "CocktailsCraft Notifications"
    private val channelDescription = "Notifications for CocktailsCraft app"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @Suppress("MissingPermission")
    actual suspend fun showNotification(notification: Notification) {
        println("PushNotificationManager: Attempting to show notification: ${notification.title}")

        if (!isPermissionGranted()) {
            println("PushNotificationManager: Permission not granted, aborting notification")
            return
        }

        println("PushNotificationManager: Permission granted, creating notification...")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_id", notification.id)
            putExtra("cocktail_id", notification.cocktailId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notification.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(getNotificationIcon(notification.type))
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 250, 250, 250))

        try {
            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                ) {
                    println("PushNotificationManager: Posting notification with ID: ${notification.id.hashCode()}")
                    notify(notification.id.hashCode(), builder.build())
                    println("PushNotificationManager: Notification posted successfully!")
                } else {
                    println("PushNotificationManager: Permission check failed at notification time")
                }
            }
        } catch (e: SecurityException) {
            println("PushNotificationManager: SecurityException when posting notification: ${e.message}")
        } catch (e: Exception) {
            println("PushNotificationManager: Unexpected error when posting notification: ${e.message}")
        }
    }

    actual suspend fun showWelcomeNotification() {
        println("PushNotificationManager: Creating welcome notification...")
        val welcomeNotification = Notification(
            id = "welcome_${System.currentTimeMillis()}",
            title = "Welcome to CocktailsCraft! 🍹",
            message = "Discover amazing cocktail recipes and start your mixology journey today!",
            type = NotificationType.SYSTEM_MESSAGE,
            timestamp = System.currentTimeMillis().toString(),
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
        // Permissions are now handled by NotificationPermissionManager
        // This method is kept for interface compatibility
    }

    actual fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    private fun getNotificationIcon(type: NotificationType): Int {
        return when (type) {
            NotificationType.NEW_COCKTAIL -> android.R.drawable.ic_menu_add
            NotificationType.FAVORITE_UPDATE -> android.R.drawable.btn_star_big_on
            NotificationType.SYSTEM_MESSAGE -> android.R.drawable.ic_dialog_info
            NotificationType.PROMOTION -> android.R.drawable.ic_dialog_alert
            NotificationType.REMINDER -> android.R.drawable.ic_menu_recent_history
        }
    }
}
