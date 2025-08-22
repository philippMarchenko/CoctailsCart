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
}

