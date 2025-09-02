package com.devphill.cocktails.domain.repository

import com.devphill.cocktails.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
    suspend fun deleteNotification(notificationId: String)
    suspend fun deleteAllReadNotifications()
    suspend fun getUnreadCount(): Int
    suspend fun getNotificationById(notificationId: String): Notification?
    suspend fun createNotification(notification: Notification)
}