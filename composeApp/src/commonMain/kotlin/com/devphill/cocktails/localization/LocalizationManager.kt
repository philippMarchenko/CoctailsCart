package com.devphill.cocktails.localization

import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the current language state throughout the app
 */
class LocalizationManager(
    private val userPreferencesManager: UserPreferencesManager
) {
    private val _currentLanguage = MutableStateFlow(getInitialLanguage())
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private fun getInitialLanguage(): Language {
        val savedLanguageCode = userPreferencesManager.getLanguage()
        return Language.fromCode(savedLanguageCode) ?: Language.ENGLISH
    }

    /**
     * Changes the app language and saves the preference
     */
    fun setLanguage(language: Language) {
        _currentLanguage.value = language
        userPreferencesManager.saveLanguage(language.code)
    }

    fun getCurrentLanguage(): Language {
        return _currentLanguage.value
    }

    companion object {
        private var INSTANCE: LocalizationManager? = null

        fun getInstance(userPreferencesManager: UserPreferencesManager): LocalizationManager {
            if (INSTANCE == null) {
                INSTANCE = LocalizationManager(userPreferencesManager)
            }
            return INSTANCE!!
        }
    }
}
