package com.devphill.cocktails.data.platform

/**
 * Platform-specific interface for opening URLs in external browsers or apps.
 */
interface UrlOpener {
    /**
     * Opens the given URL in the default browser or appropriate app.
     * For YouTube URLs, this will typically open in the YouTube app if installed,
     * or in the browser as a fallback.
     *
     * @param url The URL to open
     */
    fun openUrl(url: String)
}
