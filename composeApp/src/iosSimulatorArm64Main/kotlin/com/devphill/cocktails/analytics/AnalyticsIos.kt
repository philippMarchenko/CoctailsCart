// iOS Simulator Arm64 helper to register a simple console logger with the common Analytics facade
package com.devphill.cocktails.analytics

fun registerIosAnalytics() {
    Analytics.setLogger { screenName ->
        println("[Analytics iOS simarm64] screen: $screenName")
    }
}
