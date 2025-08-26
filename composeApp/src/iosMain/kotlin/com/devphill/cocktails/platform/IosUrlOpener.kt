package com.devphill.cocktails.platform

import platform.UIKit.UIApplication
import platform.Foundation.NSURL

/**
 * iOS implementation for opening URLs.
 * This will attempt to open YouTube URLs in the YouTube app if installed,
 * otherwise falls back to Safari browser.
 */
class IosUrlOpener : UrlOpener {

    override fun openUrl(url: String) {
        try {
            val nsUrl = NSURL.URLWithString(url)
            nsUrl?.let {
                if (UIApplication.sharedApplication.canOpenURL(it)) {
                    UIApplication.sharedApplication.openURL(it)
                }
            }
        } catch (e: Exception) {
            // Handle error - could log or show alert
            e.printStackTrace()
        }
    }
}
