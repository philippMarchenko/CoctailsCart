package com.devphill.cocktails.localization

/**
 * Supported languages in the app
 */
enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    UKRAINIAN("uk", "Українська"),
    ;

    companion object {
        fun fromCode(code: String?): Language? {
            return entries.find { it.code == code }
        }
    }
}
