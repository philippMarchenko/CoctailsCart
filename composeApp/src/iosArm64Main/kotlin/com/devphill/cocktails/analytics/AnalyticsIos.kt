// iOS Arm64 helper to register a simple console logger with the common Analytics facade
package com.devphill.cocktails.analytics

fun registerIosAnalytics() {
    Analytics.setLogger { screenName ->
        println("[Analytics iOS arm64] screen: $screenName")
    }
}
