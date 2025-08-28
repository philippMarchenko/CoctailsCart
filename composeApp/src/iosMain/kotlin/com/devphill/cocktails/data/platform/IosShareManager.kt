package com.devphill.cocktails.data.platform

import com.devphill.cocktails.data.platform.ShareManager
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
class IosShareManager : ShareManager {

    override fun shareText(text: String, subject: String?) {
        val activityItems = listOf(text)
        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        // Get the root view controller
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }

    override fun shareApp(appName: String, appUrl: String) {
        val shareText = "Check out $appName - the ultimate cocktail companion app!\n\n$appUrl"
        shareText(shareText, "Invite to $appName")
    }
}
