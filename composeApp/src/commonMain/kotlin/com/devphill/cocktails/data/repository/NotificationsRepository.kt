package com.devphill.cocktails.data.repository

import com.devphill.cocktails.data.model.Notification
import com.devphill.cocktails.data.model.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface NotificationsRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
    suspend fun deleteNotification(notificationId: String)
    suspend fun getUnreadCount(): Int
}

class NotificationsRepositoryImpl : NotificationsRepository {

    private val _notifications = MutableStateFlow(generateSampleNotifications())

    override fun getNotifications(): Flow<List<Notification>> = _notifications.asStateFlow()

    override suspend fun markAsRead(notificationId: String) {
        val currentNotifications = _notifications.value.toMutableList()
        val index = currentNotifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            currentNotifications[index] = currentNotifications[index].copy(isRead = true)
            _notifications.value = currentNotifications
        }
    }

    override suspend fun markAllAsRead() {
        val updatedNotifications = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = updatedNotifications
    }

    override suspend fun deleteNotification(notificationId: String) {
        val updatedNotifications = _notifications.value.filter { it.id != notificationId }
        _notifications.value = updatedNotifications
    }

    override suspend fun getUnreadCount(): Int {
        return _notifications.value.count { !it.isRead }
    }

    private fun generateSampleNotifications(): List<Notification> {
        return listOf(
            Notification(
                id = "1",
                title = "New Cocktail Recipe Added!",
                message = "Check out the new 'Sunset Martini' recipe that was just added to our collection.",
                type = NotificationType.NEW_COCKTAIL,
                timestamp = "2024-08-27 10:30:00",
                isRead = false,
                cocktailId = "sunset-martini"
            ),
            Notification(
                id = "2",
                title = "Weekly Favorites Update",
                message = "Your favorite cocktails have been updated with new reviews and ratings.",
                type = NotificationType.FAVORITE_UPDATE,
                timestamp = "2024-08-26 14:15:00",
                isRead = false
            ),
            Notification(
                id = "3",
                title = "Welcome to CocktailsCraft!",
                message = "Thanks for joining our community. Explore amazing cocktail recipes and share your favorites!",
                type = NotificationType.SYSTEM_MESSAGE,
                timestamp = "2024-08-23 09:00:00",
                isRead = true
            ),
            Notification(
                id = "4",
                title = "Special Offer: Premium Recipes",
                message = "Unlock exclusive premium cocktail recipes with our limited-time offer!",
                type = NotificationType.PROMOTION,
                timestamp = "2024-08-21 16:45:00",
                isRead = false
            ),
            Notification(
                id = "5",
                title = "Try Something New Today",
                message = "Haven't made a cocktail in a while? Check out these beginner-friendly recipes.",
                type = NotificationType.REMINDER,
                timestamp = "2024-08-18 11:20:00",
                isRead = true
            )
        )
    }
}
