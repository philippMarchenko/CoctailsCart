package com.devphill.coctails.data.resource

/**
 * Platform-specific resource loader interface
 * This will be implemented differently for each platform (Android, iOS, etc.)
 */
expect class PlatformResourceLoader() {
    /**
     * Initialize the resource loader with platform-specific context
     * @param context Platform-specific context (Android Context, etc.)
     */
    fun initialize(context: Any?)

    /**
     * Load a resource file as a string
     * @param fileName The name of the resource file to load
     * @return The content of the file as a string
     */
    suspend fun loadResource(fileName: String): String
}
