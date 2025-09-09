package com.devphill.cocktails.domain.interactor

import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType
import kotlinx.coroutines.flow.Flow

/**
 * Interface for notification-related business logic operations.
 *
 * This interface acts as an intermediary between ViewModels and the Repository,
 * providing a clean interface for all notification operations while encapsulating
 * business logic and data transformations.
 *
 * ## Architecture Flow
 * ViewModel → NotificationInteractor → NotificationRepository → DataSources
 */
interface NotificationInteractor {
    /**
     * Retrieves all notifications as a reactive data stream.
     * Notifications are automatically sorted by timestamp (newest first).
     *
     * @return Flow of all notifications from the repository
     */
    suspend fun getAllNotifications(): Flow<List<Notification>>

    /**
     * Retrieves only unread notifications.
     *
     * @return Flow of unread notifications
     */
    suspend fun getUnreadNotifications(): Flow<List<Notification>>

    /**
     * Retrieves notifications filtered by type.
     *
     * @param type The notification type to filter by
     * @return Flow of notifications of the specified type
     */
    suspend fun getNotificationsByType(type: NotificationType): Flow<List<Notification>>

    /**
     * Retrieves the count of unread notifications.
     *
     * @return The number of unread notifications
     */
    suspend fun getUnreadCount(): Int

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId The unique identifier of the notification to mark as read
     */
    suspend fun markNotificationAsRead(notificationId: String)

    /**
     * Marks all notifications as read.
     * This is useful for "Mark all as read" functionality.
     */
    suspend fun markAllNotificationsAsRead()

    /**
     * Deletes a specific notification.
     *
     * @param notificationId The unique identifier of the notification to delete
     */
    suspend fun deleteNotification(notificationId: String)

    /**
     * Deletes all read notifications.
     * This helps clean up the notification list while preserving unread ones.
     */
    suspend fun deleteAllReadNotifications()

    /**
     * Retrieves a specific notification by its ID.
     *
     * @param notificationId The unique identifier of the notification
     * @return The notification if found, null otherwise
     */
    suspend fun getNotificationById(notificationId: String): Notification?

    /**
     * Creates a new notification.
     * This method handles business logic validation and formatting.
     *
     * @param title The notification title
     * @param message The notification message
     * @param type The type of notification
     * @param cocktailId Optional cocktail ID if related to a specific cocktail
     * @param actionUrl Optional action URL for the notification
     */
    suspend fun insertNotification(notification: Notification)
}
