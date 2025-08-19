package com.devphill.cocktails.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidUserPreferencesManager(private val context: Context) : UserPreferencesManager {
    
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    override suspend fun setUserLoggedIn(isLoggedIn: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .apply()
    }
    
    override suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    override suspend fun setUserToken(token: String?) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(KEY_USER_TOKEN, token)
            .apply()
    }
    
    override suspend fun getUserToken(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_USER_TOKEN, null)
    }
    
    override suspend fun setUserEmail(email: String?) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }
    
    override suspend fun getUserEmail(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(KEY_USER_EMAIL, null)
    }
    
    override suspend fun clearUserData() = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_USER_TOKEN)
            .remove(KEY_USER_EMAIL)
            .apply()
    }
    
    companion object {
        private const val PREFS_NAME = "cocktails_user_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_USER_EMAIL = "user_email"
    }
}

