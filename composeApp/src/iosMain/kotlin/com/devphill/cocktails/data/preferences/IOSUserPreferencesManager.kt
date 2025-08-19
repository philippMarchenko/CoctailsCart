package com.devphill.cocktails.data.preferences

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSUserDefaults

class IOSUserPreferencesManager : UserPreferencesManager {

    private val userDefaults = NSUserDefaults.standardUserDefaults

    override suspend fun setUserLoggedIn(isLoggedIn: Boolean) = withContext(Dispatchers.Main) {
        userDefaults.setBool(isLoggedIn, KEY_IS_LOGGED_IN)
        userDefaults.synchronize()
    }

    override suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.Main) {
        userDefaults.boolForKey(KEY_IS_LOGGED_IN)
    }

    override suspend fun setUserToken(token: String?) = withContext(Dispatchers.Main) {
        if (token != null) {
            userDefaults.setObject(token, KEY_USER_TOKEN)
        } else {
            userDefaults.removeObjectForKey(KEY_USER_TOKEN)
        }
        userDefaults.synchronize()
    }

    override suspend fun getUserToken(): String? = withContext(Dispatchers.Main) {
        userDefaults.objectForKey(KEY_USER_TOKEN) as? String
    }

    override suspend fun setUserEmail(email: String?) = withContext(Dispatchers.Main) {
        if (email != null) {
            userDefaults.setObject(email, KEY_USER_EMAIL)
        } else {
            userDefaults.removeObjectForKey(KEY_USER_EMAIL)
        }
        userDefaults.synchronize()
    }

    override suspend fun getUserEmail(): String? = withContext(Dispatchers.Main) {
        userDefaults.objectForKey(KEY_USER_EMAIL) as? String
    }

    override suspend fun clearUserData() = withContext(Dispatchers.Main) {
        userDefaults.removeObjectForKey(KEY_IS_LOGGED_IN)
        userDefaults.removeObjectForKey(KEY_USER_TOKEN)
        userDefaults.removeObjectForKey(KEY_USER_EMAIL)
        userDefaults.synchronize()
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_USER_EMAIL = "user_email"
    }
}

