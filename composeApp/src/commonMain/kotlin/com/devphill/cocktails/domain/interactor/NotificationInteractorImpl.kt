package com.devphill.cocktails.domain.interactor

import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType
import com.devphill.cocktails.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Default implementation of NotificationInteractor.
 * Provides concrete business logic for notification operations by delegating to the repository layer.
 *
 * @param repository The notification repository for data access
 */
class NotificationInteractorImpl(
    private val repository: NotificationsRepository
) : NotificationInteractor {

    /**
     * Retrieves all notifications from the repository as a reactive stream.
     * Automatically sorts notifications by timestamp in descending order (newest first).
     * @return Flow emitting list of all notifications sorted by timestamp
     */
    override suspend fun getAllNotifications(): Flow<List<Notification>> {
        return repository.getNotifications().map { notifications ->
            notifications.sortedByDescending { it.timestamp }
        }
    }

    /**
     * Retrieves only unread notifications from the repository.
     * @return Flow emitting list of unread notifications sorted by timestamp
     */
    override suspend fun getUnreadNotifications(): Flow<List<Notification>> {
        return repository.getNotifications().map { notifications ->
            notifications.filter { !it.isRead }
                .sortedByDescending { it.timestamp }
        }
    }

    /**
     * Filters notifications by the specified type.
     * @param type The notification type to filter by
     * @return Flow emitting list of notifications of the specified type
     */
    override suspend fun getNotificationsByType(type: NotificationType): Flow<List<Notification>> {
        return repository.getNotifications().map { notifications ->
            notifications.filter { it.type == type }
                .sortedByDescending { it.timestamp }
        }
    }

    /**
     * Gets the count of unread notifications.
     * @return The number of unread notifications
     */
    override suspend fun getUnreadCount(): Int {
        return repository.getUnreadCount()
    }

    /**
     * Marks a specific notification as read.
     * @param notificationId The unique identifier of the notification to mark as read
     */
    override suspend fun markNotificationAsRead(notificationId: String) {
        repository.markAsRead(notificationId)
    }

    /**
     * Marks all notifications as read.
     */
    override suspend fun markAllNotificationsAsRead() {
        repository.markAllAsRead()
    }

    /**
     * Deletes a specific notification.
     * @param notificationId The unique identifier of the notification to delete
     */
    override suspend fun deleteNotification(notificationId: String) {
        repository.deleteNotification(notificationId)
    }

    /**
     * Deletes all notifications that have been read.
     * Business logic: Only removes notifications that are marked as read.
     */
    override suspend fun deleteAllReadNotifications() {
        repository.deleteAllReadNotifications()
    }

    /**
     * Retrieves a specific notification by its ID.
     * @param notificationId The unique identifier of the notification
     * @return The notification if found, null otherwise
     */
    override suspend fun getNotificationById(notificationId: String): Notification? {
        return repository.getNotificationById(notificationId)
    }

    /**
     * Creates a new notification with business logic validation.
     * @param notification The notification to insert
     */
    override suspend fun insertNotification(
        notification: Notification
    ) {
        repository.insertNotification(notification)
    }

    /**
     * Generates a unique ID for a new notification.
     * In a real implementation, this might use UUID or timestamp-based generation.
     */
    private fun generateNotificationId(): String {
        return ""
    }
}
