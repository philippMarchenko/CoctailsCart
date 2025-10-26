package com.devphill.cocktails.localization

import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the current language state throughout the app
 */
open class LocalizationManager(
    private val userPreferencesManager: UserPreferencesManager,
    private val localeConfiguration: LocaleConfiguration,
) {
    private val _currentLanguage = MutableStateFlow(getInitialLanguage())
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    init {
        initialize()
    }

    private fun getInitialLanguage(): Language {
        val savedLanguageCode = userPreferencesManager.getLanguage()
        return Language.fromCode(savedLanguageCode) ?: Language.ENGLISH
    }

    /**
     * Changes the app language and saves the preference
     * Also configures the system locale to ensure proper string resource selection
     */
    fun setLanguage(language: Language) {
        _currentLanguage.value = language
        userPreferencesManager.saveLanguage(language.code)
        // Configure the system locale for immediate effect
        localeConfiguration.configureLocale(language)
    }

    fun getCurrentLanguage(): Language {
        return _currentLanguage.value
    }

    /**
     * Initialize the locale configuration based on saved preferences
     * Call this during app startup
     */
    fun initialize() {
        val currentLang = getCurrentLanguage()
        localeConfiguration.configureLocale(currentLang)
    }
}
