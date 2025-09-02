package com.devphill.cocktails.domain.repository

import com.devphill.cocktails.data.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
    suspend fun deleteNotification(notificationId: String)
    suspend fun getUnreadCount(): Int
}