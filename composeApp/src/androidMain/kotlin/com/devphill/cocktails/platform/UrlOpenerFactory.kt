package com.devphill.cocktails.platform

import android.content.Context

/**
 * Android-specific factory function to create UrlOpener instance
 */
fun createUrlOpener(context: Context): UrlOpener {
    return AndroidUrlOpener(context)
}
