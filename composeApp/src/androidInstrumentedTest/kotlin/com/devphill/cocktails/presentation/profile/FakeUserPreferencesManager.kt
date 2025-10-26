package com.devphill.cocktails.presentation.profile

import com.devphill.cocktails.data.auth.User
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.presentation.theme.ThemeMode

class FakeUserPreferencesManager : UserPreferencesManager {
    private var user: User? = null
    private var themeMode: ThemeMode? = null
    private var language: String? = null
    private val booleans = mutableMapOf<String, Boolean>()

    override suspend fun saveUser(user: User?) { this.user = user }
    override suspend fun getUser(): User? = user
    override suspend fun clearUserData() { user = null }
    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean = booleans[key] ?: defaultValue
    override suspend fun putBoolean(key: String, value: Boolean) { booleans[key] = value }
    override fun saveThemeMode(themeMode: ThemeMode) { this.themeMode = themeMode }
    override fun getThemeMode(): ThemeMode? = themeMode
    override fun clearThemeMode() { themeMode = null }
    override fun getLanguage(): String? = language
    override fun saveLanguage(languageCode: String) { language = languageCode }
}

