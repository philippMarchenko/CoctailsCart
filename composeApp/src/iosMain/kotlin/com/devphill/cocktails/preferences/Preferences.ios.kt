package com.devphill.cocktails.preferences

import com.devphill.cocktails.data.preferences.IOSUserPreferencesManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager

actual fun createUserPreferencesManager(context: Any?): UserPreferencesManager {
    return IOSUserPreferencesManager()
}
