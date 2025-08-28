package com.devphill.cocktails.data.model

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: String, // Using String instead of LocalDateTime for simplicity
    val isRead: Boolean = false,
    val cocktailId: String? = null, // Optional reference to a cocktail
    val actionUrl: String? = null // Optional action URL
)

enum class NotificationType {
    NEW_COCKTAIL,
    FAVORITE_UPDATE,
    SYSTEM_MESSAGE,
    PROMOTION,
    REMINDER
}
