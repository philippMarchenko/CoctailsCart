package com.devphill.cocktails.di

import com.devphill.cocktails.data.platform.UrlOpener
import com.devphill.cocktails.data.platform.createUrlOpener
import com.devphill.cocktails.data.platform.ShareManager
import com.devphill.cocktails.data.platform.IosShareManager
import org.koin.dsl.module

/**
 * Platform-specific Koin module for iOS
 * This module provides platform-specific dependencies like UrlOpener
 */
val platformModule = module {
    single<UrlOpener> {
        createUrlOpener()
    }
    single<ShareManager> {
        IosShareManager()
    }
}
