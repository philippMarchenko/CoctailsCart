package com.devphill.cocktails.domain.model

/**
 * Represents a notification message in the CocktailsCraft application.
 * Contains all necessary information for displaying and managing user notifications.
 *
 * @property id Unique identifier for the notification
 * @property title The notification headline text
 * @property message The detailed notification content
 * @property type Classification of the notification for proper handling
 * @property timestamp When the notification was created (ISO string format)
 * @property isRead Whether the user has read this notification
 * @property cocktailId Optional reference to a specific cocktail if notification is cocktail-related
 * @property actionUrl Optional deep link URL for notification actions
 */
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: String, // Using String instead of LocalDateTime for simplicity
    val isRead: Boolean = false,
    val cocktailId: String? = null, // Optional reference to a cocktail
    val actionUrl: String? = null, // Optional action URL
)

/**
 * Enumeration of notification types for proper categorization and handling.
 * Used to determine notification behavior, styling, and routing.
 */
enum class NotificationType {
    /** Notification about newly added cocktails to the database */
    NEW_COCKTAIL,

    /** Notification about changes to user's favorite cocktails */
    FAVORITE_UPDATE,

    /** System-related messages and announcements */
    SYSTEM_MESSAGE,

    /** Marketing and promotional notifications */
    PROMOTION,

    /** Reminder notifications for user actions */
    REMINDER,
}
