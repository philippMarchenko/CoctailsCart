package com.devphill.cocktails.di

import com.devphill.cocktails.platform.UrlOpener
import com.devphill.cocktails.platform.createUrlOpener
import org.koin.dsl.module

/**
 * Platform-specific Koin module for iOS
 * This module provides platform-specific dependencies like UrlOpener
 */
val platformModule = module {
    single<UrlOpener> {
        createUrlOpener()
    }
}
