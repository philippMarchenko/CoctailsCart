package com.devphill.cocktails.data.repository

import com.devphill.cocktails.data.database.CocktailDatabase
import com.devphill.cocktails.data.database.mapper.toDomain
import com.devphill.cocktails.data.database.mapper.toEntity
import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType
import com.devphill.cocktails.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationsRepositoryImpl(
    private val database: CocktailDatabase
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

    override suspend fun createNotification(notification: Notification) {
        notificationDao.insertNotification(notification.toEntity())
    }

    /**
     * Initialize the database with some sample notifications on first run
     * This method should be called only once when the app is first installed
     */
    suspend fun initializeSampleNotifications() {
        val count = notificationDao.getUnreadCount() + getReadNotificationsCount()
        if (count == 0) {
            val sampleNotifications = generateSampleNotifications()
            notificationDao.insertNotifications(sampleNotifications.map { it.toEntity() })
        }
    }

    private suspend fun getReadNotificationsCount(): Int {
        // Get total count minus unread count to get read count
        return notificationDao.getAllNotifications().map { it.size }.let { flow ->
            var totalCount = 0
            flow.collect { totalCount = it }
            totalCount
        } - notificationDao.getUnreadCount()
    }

    private fun generateSampleNotifications(): List<Notification> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            Notification(
                id = "sample_1",
                title = "New Cocktail Recipe Added!",
                message = "Check out the new 'Sunset Martini' recipe that was just added to our collection.",
                type = NotificationType.NEW_COCKTAIL,
                timestamp = (currentTime - 86400000).toString(), // 1 day ago
                isRead = false,
                cocktailId = "sunset-martini"
            ),
            Notification(
                id = "sample_2",
                title = "Weekly Favorites Update",
                message = "Your favorite cocktails have been updated with new reviews and ratings.",
                type = NotificationType.FAVORITE_UPDATE,
                timestamp = (currentTime - 172800000).toString(), // 2 days ago
                isRead = false
            ),
            Notification(
                id = "sample_3",
                title = "Welcome to CocktailsCraft!",
                message = "Thanks for joining our community. Explore amazing cocktail recipes and share your favorites!",
                type = NotificationType.SYSTEM_MESSAGE,
                timestamp = (currentTime - 604800000).toString(), // 1 week ago
                isRead = true
            ),
            Notification(
                id = "sample_4",
                title = "Special Offer: Premium Recipes",
                message = "Unlock exclusive premium cocktail recipes with our limited-time offer!",
                type = NotificationType.PROMOTION,
                timestamp = (currentTime - 1209600000).toString(), // 2 weeks ago
                isRead = false
            ),
            Notification(
                id = "sample_5",
                title = "Try Something New Today",
                message = "Haven't made a cocktail in a while? Check out these beginner-friendly recipes.",
                type = NotificationType.REMINDER,
                timestamp = (currentTime - 1814400000).toString(), // 3 weeks ago
                isRead = true
            )
        )
    }
}
