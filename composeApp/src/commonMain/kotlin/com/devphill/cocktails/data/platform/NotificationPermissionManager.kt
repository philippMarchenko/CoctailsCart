package com.devphill.cocktails.data.platform

/**
 * Platform-specific interface for requesting notification permissions from UI
 */
expect class NotificationPermissionManager {
    fun requestPermissionIfNeeded()
    fun isPermissionGranted(): Boolean
}
