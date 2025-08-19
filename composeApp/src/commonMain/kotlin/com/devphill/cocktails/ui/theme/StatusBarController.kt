package com.devphill.cocktails.ui.theme

/**
 * Common interface for controlling status bar appearance
 */
expect class StatusBarController {
    /**
     * Set the status bar appearance to light or dark
     * @param isLight true for light status bar (dark icons), false for dark status bar (light icons)
     */
    fun setStatusBarAppearance(isLight: Boolean)
}
