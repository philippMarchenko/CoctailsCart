package com.devphill.cocktails.presentation.theme

import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
}

class ThemeManager(private val preferencesManager: UserPreferencesManager) {
    private val _currentTheme = MutableStateFlow(ThemeMode.DARK)
    val currentTheme: StateFlow<ThemeMode> = _currentTheme.asStateFlow()

    init {
        // Load saved theme on initialization
        val savedTheme = preferencesManager.getThemeMode()
        if (savedTheme != null) {
            _currentTheme.value = savedTheme
        }
    }

    fun setTheme(theme: ThemeMode) {
        _currentTheme.value = theme
        // Save to preferences
        preferencesManager.saveThemeMode(theme)

        // Update status bar appearance
    //    updateStatusBarForTheme(theme)
    }

    fun getCurrentTheme(): ThemeMode = _currentTheme.value

    fun initializeTheme(savedTheme: ThemeMode?) {
        savedTheme?.let {
            _currentTheme.value = it
            preferencesManager.saveThemeMode(it)
        }
    }


}


