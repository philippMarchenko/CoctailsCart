package com.devphill.cocktails.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.devphill.cocktails.data.auth.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AndroidUserPreferencesManager(private val context: Context) : UserPreferencesManager {
    
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    private val json = Json { ignoreUnknownKeys = true }

    // New implementation - save complete User object
    override suspend fun saveUser(user: User?) = withContext(Dispatchers.IO) {
        if (user != null) {
            val userJson = json.encodeToString(user)
            sharedPreferences.edit()
                .putString(KEY_USER_OBJECT, userJson)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply()
        } else {
            clearUserData()
        }
    }

    override suspend fun getUser(): User? = withContext(Dispatchers.IO) {
        val userJson = sharedPreferences.getString(KEY_USER_OBJECT, null)
        return@withContext if (userJson != null) {
            try {
                json.decodeFromString<User>(userJson)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    override suspend fun setUserLoggedIn(isLoggedIn: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .apply()
    }
    
    override suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    override suspend fun clearUserData() = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_USER_TOKEN)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_DISPLAY_NAME)
            .remove(KEY_USER_PHOTO_URL)
            .remove(KEY_USER_UID)
            .remove(KEY_USER_OBJECT)
            .apply()
    }

    // Generic preference methods
    override suspend fun putBoolean(key: String, value: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(key, defaultValue)
    }

    override suspend fun putString(key: String, value: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    override suspend fun getString(key: String, defaultValue: String?): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(key, defaultValue)
    }

    companion object {
        private const val PREFS_NAME = "cocktails_user_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_DISPLAY_NAME = "user_display_name"
        private const val KEY_USER_PHOTO_URL = "user_photo_url"
        private const val KEY_USER_UID = "user_uid"
        private const val KEY_USER_OBJECT = "user_object"
    }
}
