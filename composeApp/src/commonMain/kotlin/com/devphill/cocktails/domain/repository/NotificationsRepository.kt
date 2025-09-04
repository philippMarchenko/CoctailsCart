package com.devphill.cocktails.domain.repository

import com.devphill.cocktails.domain.model.Notification
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing notifications.
 *
 * This interface abstracts the data operations related to notifications,
 * providing a clean API for the rest of the application to interact with
 * notification data without concerning itself with the underlying data source.
 *
 * ## Architecture Flow
 * ViewModel → NotificationInteractor → NotificationsRepository → DataSources
 */
interface NotificationsRepository {
    /**
     * Retrieves all notifications as a reactive data stream.
     *
     * @return Flow emitting list of all notifications
     */
    fun getNotifications(): Flow<List<Notification>>

    /**
     * Marks a specific notification as read.
     *
     * This operation updates the read status of a notification identified by its unique ID.
     * Once marked as read, the notification's `isRead` property will be set to true.
     *
     * @param notificationId The unique identifier of the notification to mark as read
     * @throws IllegalArgumentException if the notification ID is empty or invalid
     */
    suspend fun markAsRead(notificationId: String)

    /**
     * Marks all notifications as read.
     *
     * This is a bulk operation that updates the read status of all notifications
     * in the repository to read. This is typically used when the user wants to
     * clear all unread notification indicators at once.
     */
    suspend fun markAllAsRead()

    /**
     * Deletes a specific notification from the repository.
     *
     * This operation permanently removes a notification identified by its unique ID.
     * Once deleted, the notification cannot be recovered.
     *
     * @param notificationId The unique identifier of the notification to delete
     * @throws IllegalArgumentException if the notification ID is empty or invalid
     */
    suspend fun deleteNotification(notificationId: String)

    /**
     * Deletes all notifications that have been marked as read.
     *
     * This is a cleanup operation that removes read notifications to keep the
     * notification list manageable. Unread notifications will remain untouched.
     * This operation is useful for periodic cleanup of old notifications.
     */
    suspend fun deleteAllReadNotifications()

    /**
     * Retrieves the count of unread notifications.
     *
     * This method provides a quick way to get the number of notifications
     * that haven't been read yet. This is commonly used for displaying
     * notification badges or indicators in the UI.
     *
     * @return The number of unread notifications
     */
    suspend fun getUnreadCount(): Int

    /**
     * Retrieves a specific notification by its unique identifier.
     *
     * This method allows fetching a single notification when you know its ID.
     * This is useful for displaying notification details or performing
     * operations on a specific notification.
     *
     * @param notificationId The unique identifier of the notification to retrieve
     * @return The notification if found, null if no notification exists with the given ID
     * @throws IllegalArgumentException if the notification ID is empty or invalid
     */
    suspend fun getNotificationById(notificationId: String): Notification?

    /**
     * Inserts a new notification into the repository.
     *
     * This method adds a new notification to the data store. The notification
     * should have all required fields populated. If a notification with the
     * same ID already exists, the behavior depends on the implementation
     * (it may update the existing notification or throw an exception).
     *
     * @param notification The notification object to insert
     * @throws IllegalArgumentException if the notification object is invalid
     */
    suspend fun insertNotification(notification: Notification)
}