package com.devphill.cocktails.analytics

/**
 * Simple common Analytics facade.
 * Platform code can register a logger via [setLogger] to receive screen events.
 */
object Analytics {
    private var logger: ((String, Map<String, String>?) -> Unit)? = null

    /**
     * Optional platform-specific initialization hook. Kept for compatibility with previous calls.
     */
    fun init(context: Any) {
        // no-op in common module; platforms should call their registration helper
    }

    /**
     * Register a platform logger which will be called when screens are logged.
     * Logger receives the screen name and an optional map of parameters.
     */
    fun setLogger(l: (String, Map<String, String>?) -> Unit) {
        logger = l
    }

    /**
     * Log a screen open. Will delegate to the platform logger if registered.
     */
    fun logScreen(screenName: String, params: Map<String, String>? = null) {
        logger?.invoke(screenName, params)
    }
}
