package com.devphill.cocktails.localization

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults

/**
 * iOS-specific implementation for configuring locale
 */
class IosLocaleConfiguration : LocaleConfiguration {

    override fun configureLocale(language: Language) {
        // Set the locale in UserDefaults which affects the app's locale
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.setObject(arrayListOf(language.code), forKey = "AppleLanguages")
        userDefaults.synchronize()

        // Also set the current locale
      //  NSLocale.setCurrentLocale(NSLocale(localeIdentifier = language.code))
    }

    override fun getCurrentLocale(): String {
        val userDefaults = NSUserDefaults.standardUserDefaults
        val languages = userDefaults.objectForKey("AppleLanguages") as? List<*>
        return languages?.firstOrNull() as? String ?: "en"
    }
}
