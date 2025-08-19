package com.devphill.cocktails.data.preferences

/**
 * Interface for managing user preferences across platforms
 */
interface UserPreferencesManager {
    suspend fun setUserLoggedIn(isLoggedIn: Boolean)
    suspend fun isUserLoggedIn(): Boolean
    suspend fun setUserToken(token: String?)
    suspend fun getUserToken(): String?
    suspend fun setUserEmail(email: String?)
    suspend fun getUserEmail(): String?
    suspend fun clearUserData()
}

