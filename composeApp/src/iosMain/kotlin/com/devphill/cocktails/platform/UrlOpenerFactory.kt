package com.devphill.cocktails.platform

/**
 * iOS-specific factory function to create UrlOpener instance
 */
fun createUrlOpener(): UrlOpener {
    return IosUrlOpener()
}
