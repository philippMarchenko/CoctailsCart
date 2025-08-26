package com.devphill.cocktails.platform

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Android implementation for opening URLs.
 * This will attempt to open YouTube URLs in the YouTube app if installed,
 * otherwise falls back to the default browser.
 */
class AndroidUrlOpener(private val context: Context) : UrlOpener {

    override fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handle error - could log or show toast
            e.printStackTrace()
        }
    }
}
