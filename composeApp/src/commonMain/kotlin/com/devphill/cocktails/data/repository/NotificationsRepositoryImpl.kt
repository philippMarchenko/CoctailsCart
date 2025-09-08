package com.devphill.cocktails.data.repository

import com.devphill.cocktails.data.database.CocktailDatabase
import com.devphill.cocktails.data.database.mapper.toDomain
import com.devphill.cocktails.data.database.mapper.toEntity
import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationsRepositoryImpl(
    database: CocktailDatabase,
) : NotificationsRepository {
    private val notificationDao = database.notificationDao()

    override fun getNotifications(): Flow<List<Notification>> {
        return notificationDao.getAllNotifications().map { entities ->
            entities.toDomain()
        }
    }

    override suspend fun markAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
    }

    override suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
    }

    override suspend fun deleteNotification(notificationId: String) {
        notificationDao.deleteNotification(notificationId)
    }

    override suspend fun deleteAllReadNotifications() {
        notificationDao.deleteAllReadNotifications()
    }

    override suspend fun getUnreadCount(): Int {
        return notificationDao.getUnreadCount()
    }

    override suspend fun getNotificationById(notificationId: String): Notification? {
        return notificationDao.getNotificationById(notificationId)?.toDomain()
    }

    override suspend fun insertNotification(notification: Notification) {
        notificationDao.insertNotification(notification.toEntity())
    }
}
