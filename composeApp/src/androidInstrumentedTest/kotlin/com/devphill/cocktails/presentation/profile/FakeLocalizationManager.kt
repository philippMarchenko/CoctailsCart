package com.devphill.cocktails.presentation.profile

import com.devphill.cocktails.localization.LocalizationManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.localization.LocaleConfiguration
import com.devphill.cocktails.localization.Language

class FakeLocalizationManager(userPreferencesManager: UserPreferencesManager) : LocalizationManager(
    userPreferencesManager,
    object : LocaleConfiguration {
        override fun configureLocale(language: Language) { /* no-op for test */ }
        override fun getCurrentLocale(): String {
            TODO("Not yet implemented")
        }
    }
)

