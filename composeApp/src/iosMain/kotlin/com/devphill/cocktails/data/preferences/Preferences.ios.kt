package com.devphill.cocktails.data.preferences

actual fun createUserPreferencesManager(context: Any?): UserPreferencesManager {
    return IOSUserPreferencesManager()
}
