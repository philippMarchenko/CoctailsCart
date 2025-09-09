package com.devphill.cocktails.localization

/**
 * Platform-specific interface for configuring locale
 * This allows us to set the system locale programmatically on each platform
 */
interface LocaleConfiguration {
    /**
     * Configures the system locale for the given language
     * This will make the system pick up the correct string resources
     */
    fun configureLocale(language: Language)

    /**
     * Gets the current system locale
     */
    fun getCurrentLocale(): String
}
