package com.devphill.cocktails.data.preferences

import com.devphill.cocktails.data.auth.User
import com.devphill.cocktails.presentation.theme.ThemeMode

interface UserPreferencesManager {
    // User authentication preferences
    suspend fun saveUser(user: User?)

    suspend fun getUser(): User?

    suspend fun clearUserData()

    // Base key-value preferences
    suspend fun getBoolean(
        key: String,
        defaultValue: Boolean,
    ): Boolean

    suspend fun putBoolean(
        key: String,
        value: Boolean,
    )

    // Theme preferences
    fun saveThemeMode(themeMode: ThemeMode)

    fun getThemeMode(): ThemeMode?

    fun clearThemeMode()
}
