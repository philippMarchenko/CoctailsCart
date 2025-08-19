package com.devphill.cocktails.data.resource

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of PlatformResourceLoader
 * Loads resources from the assets folder
 */
actual class PlatformResourceLoader {

    private var context: Context? = null

    actual fun initialize(context: Any?) {
        this.context = (context as? Context)?.applicationContext
            ?: throw IllegalArgumentException("Android PlatformResourceLoader requires a Context")
    }

    actual suspend fun loadResource(fileName: String): String = withContext(Dispatchers.IO) {
        try {
            val appContext = context
                ?: throw IllegalStateException("PlatformResourceLoader not initialized. Call initialize(context) first.")

            appContext.assets.open(fileName).bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: Exception) {
            throw Exception("Failed to load resource file: $fileName", e)
        }
    }
}
