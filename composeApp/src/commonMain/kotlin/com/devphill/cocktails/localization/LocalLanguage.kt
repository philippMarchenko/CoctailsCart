package com.devphill.cocktails.localization

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for providing the current language throughout the Compose tree
 */
val LocalLanguage = staticCompositionLocalOf { Language.ENGLISH }
