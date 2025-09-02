package com.devphill.cocktails.data.platform

/**
 * Platform-specific interface for sharing content outside the application.
 * Provides unified API for sharing text content and app recommendations across different platforms.
 */
interface ShareManager {
    /**
     * Shares plain text content using the platform's native sharing mechanism.
     * Opens the system share dialog with available sharing options.
     * @param text The text content to share
     * @param subject Optional subject line for the shared content (used in email clients)
     */
    fun shareText(text: String, subject: String? = null)

    /**
     * Shares the application with others using platform-specific app store links.
     * Promotes the app by sharing its download link and description.
     * @param appName The display name of the application
     * @param appUrl The platform-specific app store URL for downloading the app
     */
    fun shareApp(appName: String, appUrl: String)
}
