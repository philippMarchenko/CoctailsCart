package com.devphill.cocktails.data.auth

import android.content.Context

actual fun createAuthManager(context: Any?): AuthManager {
    return AndroidAuthManager(context as Context)
}

