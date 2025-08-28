package com.devphill.cocktails.data.platform

/**
 * iOS-specific factory function to create UrlOpener instance
 */
fun createUrlOpener(): UrlOpener {
    return IosUrlOpener()
}
