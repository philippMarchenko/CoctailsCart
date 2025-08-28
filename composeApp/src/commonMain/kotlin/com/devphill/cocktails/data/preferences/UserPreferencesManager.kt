package com.devphill.cocktails.data.preferences

import com.devphill.cocktails.data.auth.User

/**
 * Interface for managing user preferences across platforms
 */
interface UserPreferencesManager {
    suspend fun setUserLoggedIn(isLoggedIn: Boolean)
    suspend fun isUserLoggedIn(): Boolean
    suspend fun saveUser(user: User?)
    suspend fun getUser(): User?
    suspend fun clearUserData()

    // Generic preference methods for storing any key-value pairs
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String, defaultValue: String? = null): String?
}
