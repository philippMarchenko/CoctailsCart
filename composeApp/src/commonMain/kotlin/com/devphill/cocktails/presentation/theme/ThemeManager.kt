package com.devphill.cocktails.presentation.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

class ThemeManager {
    private val _currentTheme = MutableStateFlow(ThemeMode.DARK)
    val currentTheme: StateFlow<ThemeMode> = _currentTheme.asStateFlow()

    fun setTheme(theme: ThemeMode) {
        _currentTheme.value = theme
        // TODO: Save to preferences when UserPreferencesManager is available
        
        // Update status bar appearance
        updateStatusBarForTheme(theme)
    }

    fun getCurrentTheme(): ThemeMode = _currentTheme.value
    
    fun initializeTheme(savedTheme: ThemeMode?) {
        savedTheme?.let { _currentTheme.value = it }
    }
    
    private fun updateStatusBarForTheme(theme: ThemeMode) {
        // This will be implemented by platform-specific code
        when (theme) {
            ThemeMode.LIGHT -> {
                // Light theme: use dark status bar icons
                updateStatusBarAppearance(isLight = true)
            }
            ThemeMode.DARK -> {
                // Dark theme: use light status bar icons
                updateStatusBarAppearance(isLight = false)
            }
            ThemeMode.SYSTEM -> {
                // System theme: let system decide
                // For now, default to dark theme behavior
                updateStatusBarAppearance(isLight = false)
            }
        }
    }
}

// Global theme manager instance
object GlobalThemeManager {
    private val themeManager = ThemeManager()
    
    fun getThemeManager(): ThemeManager = themeManager
}

/**
 * Platform-specific function to update status bar appearance
 */
expect fun updateStatusBarAppearance(isLight: Boolean)
