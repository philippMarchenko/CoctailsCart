package com.devphill.cocktails.data.platform

/**
 * Platform-specific interface for managing notification permissions.
 * Handles requesting and checking notification permissions across different platforms (Android, iOS).
 */
expect class NotificationPermissionManager {
    /**
     * Requests notification permission from the user if not already granted.
     * Shows platform-specific permission dialog and handles user response.
     */
    fun requestPermissionIfNeeded()

    /**
     * Checks if notification permission has been granted by the user.
     * @return True if permission is granted, false otherwise
     */
    fun isPermissionGranted(): Boolean
}
