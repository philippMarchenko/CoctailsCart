// iOS helper to register a simple console logger with the common Analytics facade
package com.devphill.cocktails.analytics

fun registerIosAnalytics(deviceName: String, osVersion: String) {
    // Register a logger that prints the screen, device name and OS version, and any additional params
    Analytics.setLogger { screenName, params ->
        val paramsStr = params?.entries?.joinToString(", ") { "${'$'}{it.key}=${'$'}{it.value}" } ?: ""
        println("[Analytics iOS] screen: ${'$'}screenName, device_name: ${'$'}deviceName, os_version: ${'$'}osVersion ${'$'}paramsStr")
    }
}
