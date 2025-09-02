package com.devphill.cocktails.data.preferences

import com.devphill.cocktails.data.auth.User

/**
 * Interface for managing user preferences and settings across platforms.
 * Provides unified API for storing user authentication state, user data, and general preferences.
 */
interface UserPreferencesManager {
    /**
     * Sets the user's login status in persistent storage.
     * @param isLoggedIn True if user is logged in, false otherwise
     */
    suspend fun setUserLoggedIn(isLoggedIn: Boolean)

    /**
     * Checks if a user is currently logged in based on stored preferences.
     * @return True if user is logged in, false otherwise
     */
    suspend fun isUserLoggedIn(): Boolean

    /**
     * Saves user data to persistent storage.
     * @param user The user object to save, null to clear stored user data
     */
    suspend fun saveUser(user: User?)

    /**
     * Retrieves the currently stored user data.
     * @return User object if saved, null if no user data is stored
     */
    suspend fun getUser(): User?

    /**
     * Clears all user-related data from preferences.
     * Removes login status, user data, and any user-specific settings.
     */
    suspend fun clearUserData()

    /**
     * Stores a boolean preference value.
     * @param key The preference key identifier
     * @param value The boolean value to store
     */
    suspend fun putBoolean(key: String, value: Boolean)

    /**
     * Retrieves a boolean preference value.
     * @param key The preference key identifier
     * @param defaultValue Default value to return if key doesn't exist
     * @return Stored boolean value or defaultValue if not found
     */
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    /**
     * Stores a string preference value.
     * @param key The preference key identifier
     * @param value The string value to store
     */
    suspend fun putString(key: String, value: String)

    /**
     * Retrieves a string preference value.
     * @param key The preference key identifier
     * @param defaultValue Default value to return if key doesn't exist
     * @return Stored string value or defaultValue if not found
     */
    suspend fun getString(key: String, defaultValue: String? = null): String?
}
