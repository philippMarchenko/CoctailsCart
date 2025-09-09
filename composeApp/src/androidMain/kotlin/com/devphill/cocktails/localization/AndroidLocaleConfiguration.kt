package com.devphill.cocktails.localization

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

/**
 * Android-specific implementation for configuring locale
 */
class AndroidLocaleConfiguration(private val context: Context) : LocaleConfiguration {

    override fun configureLocale(language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    override fun getCurrentLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale.language
        }
    }
}
