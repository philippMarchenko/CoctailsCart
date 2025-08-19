package com.devphill.cocktails.auth

import android.content.Context

actual fun createAuthManager(context: Any?): AuthManager {
    return AndroidAuthManager(context as Context)
}

