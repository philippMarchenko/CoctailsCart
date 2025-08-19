package com.devphill.cocktails.data.preferences

import android.content.Context

actual fun createUserPreferencesManager(context: Any?): UserPreferencesManager {
    return AndroidUserPreferencesManager(context as Context)
}

