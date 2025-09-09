package com.devphill.cocktails.data.preferences

import com.devphill.cocktails.data.auth.User
import com.devphill.cocktails.presentation.theme.ThemeMode
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

class UserPreferencesManagerImpl() : UserPreferencesManager {

    val settings: Settings = Settings()

    companion object {
        private const val THEME_MODE_KEY = "theme_mode"
        private const val USER_KEY = "user"
        const val IS_LOGGED_IN_KEY = "is_logged_in"
        private val LANGUAGE_CODE_KEY = "language_code"
    }

    private val json = Json { ignoreUnknownKeys = true }

    // User authentication preferences
    override suspend fun saveUser(user: User?) {
        if (user != null) {
            try {
                val userJson = json.encodeToString(user)
                settings.putString(USER_KEY, userJson)
            } catch (e: Exception) {
                // Fallback to simple string format if serialization fails
                settings.putString(USER_KEY, "${user.displayName ?: ""}|${user.email ?: ""}|${user.photoUrl ?: ""}")
            }
        } else {
            settings.remove(USER_KEY)
        }
    }

    override suspend fun getUser(): User? {
        val userJson = settings.getStringOrNull(USER_KEY) ?: return null

        return try {
            json.decodeFromString<User>(userJson)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun clearUserData() {
        settings.remove(USER_KEY)
        settings.remove(IS_LOGGED_IN_KEY)
    }

    // Theme preferences
    override fun saveThemeMode(themeMode: ThemeMode) {
        settings.putString(THEME_MODE_KEY, themeMode.name)
    }

    override fun getThemeMode(): ThemeMode? {
        val themeModeString = settings.getStringOrNull(THEME_MODE_KEY)
        return themeModeString?.let {
            try {
                ThemeMode.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    override fun saveLanguage(languageCode: String) {
        settings.putString(LANGUAGE_CODE_KEY, languageCode)
    }

    override fun getLanguage(): String? {
        return settings.getStringOrNull(LANGUAGE_CODE_KEY)
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    override fun clearThemeMode() {
        settings.remove(THEME_MODE_KEY)
    }
}
