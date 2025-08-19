package com.devphill.cocktails.data.resource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

/**
 * iOS implementation of PlatformResourceLoader
 * Loads resources from the app bundle
 */
actual class PlatformResourceLoader {

    actual fun initialize(context: Any?) {
        // iOS doesn't need initialization as it uses NSBundle.mainBundle directly
        // This method is provided to satisfy the common interface
    }

    actual suspend fun loadResource(fileName: String): String = withContext(Dispatchers.Default) {
        val bundle = NSBundle.mainBundle
        val path = bundle.pathForResource(fileName.substringBeforeLast("."), ofType = fileName.substringAfterLast("."))
            ?: throw Exception("Resource file not found: $fileName")

        val content = NSString.stringWithContentsOfFile(
            path = path,
            encoding = NSUTF8StringEncoding,
            error = null
        ) ?: throw Exception("Failed to read file content: $fileName")

        content
    }
}
