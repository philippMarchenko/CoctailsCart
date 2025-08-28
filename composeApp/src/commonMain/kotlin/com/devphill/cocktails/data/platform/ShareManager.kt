package com.devphill.cocktails.data.platform

interface ShareManager {
    fun shareText(text: String, subject: String? = null)
    fun shareApp(appName: String, appUrl: String)
}
