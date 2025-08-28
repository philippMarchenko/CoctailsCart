package com.devphill.cocktails.data.platform

import android.content.Context
import android.content.Intent
import com.devphill.cocktails.data.platform.ShareManager

class AndroidShareManager(private val context: Context) : ShareManager {

    override fun shareText(text: String, subject: String?) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        }

        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    override fun shareApp(appName: String, appUrl: String) {
        val shareText = "Check out $appName - the ultimate cocktail companion app!\n\n$appUrl"
        shareText(shareText, "Invite to $appName")
    }
}
