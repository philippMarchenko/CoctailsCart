package com.devphill.cocktails.data.preferences

import com.devphill.cocktails.data.auth.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IOSUserPreferencesManager : UserPreferencesManager {

    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun saveUser(user: User?) = withContext(Dispatchers.Main) {
        if (user != null) {
            val userJson = json.encodeToString(user)
            userDefaults.setObject(userJson, KEY_USER_OBJECT)
            userDefaults.setBool(true, KEY_IS_LOGGED_IN)
        } else {
            clearUserData()
        }
        userDefaults.synchronize()
    }

    override suspend fun getUser(): User? = withContext(Dispatchers.Main) {
        val userJson = userDefaults.stringForKey(KEY_USER_OBJECT)
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

    override suspend fun setUserLoggedIn(isLoggedIn: Boolean) = withContext(Dispatchers.Main) {
        userDefaults.setBool(isLoggedIn, KEY_IS_LOGGED_IN)
        userDefaults.synchronize()
    }

    override suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.Main) {
        userDefaults.boolForKey(KEY_IS_LOGGED_IN)
    }

    override suspend fun clearUserData() = withContext(Dispatchers.Main) {
        userDefaults.removeObjectForKey(KEY_IS_LOGGED_IN)
        userDefaults.removeObjectForKey(KEY_USER_TOKEN)
        userDefaults.removeObjectForKey(KEY_USER_EMAIL)
        userDefaults.removeObjectForKey(KEY_USER_OBJECT)
        userDefaults.synchronize()
    }

    // Generic preference methods
    override suspend fun putBoolean(key: String, value: Boolean) = withContext(Dispatchers.Main) {
        userDefaults.setBool(value, key)
        userDefaults.synchronize()
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean = withContext(Dispatchers.Main) {
        if (userDefaults.objectForKey(key) != null) {
            userDefaults.boolForKey(key)
        } else {
            defaultValue
        }
    }

    override suspend fun putString(key: String, value: String) = withContext(Dispatchers.Main) {
        userDefaults.setObject(value, key)
        userDefaults.synchronize()
    }

    override suspend fun getString(key: String, defaultValue: String?): String? = withContext(Dispatchers.Main) {
        userDefaults.stringForKey(key) ?: defaultValue
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_OBJECT = "user_object"
    }
}
