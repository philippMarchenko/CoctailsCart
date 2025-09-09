package com.devphill.cocktails.di

import com.devphill.cocktails.data.auth.AuthManager
import com.devphill.cocktails.data.auth.createAuthManager
import com.devphill.cocktails.data.database.CocktailDatabase
import com.devphill.cocktails.data.database.datasource.DatabaseCocktailDataSourceImpl
import com.devphill.cocktails.data.database.getDatabaseBuilder
import com.devphill.cocktails.data.datasource.LocalCocktailsDataSourceImpl
import com.devphill.cocktails.data.platform.AndroidShareManager
import com.devphill.cocktails.data.platform.NotificationPermissionManager
import com.devphill.cocktails.data.platform.PushNotificationManager
import com.devphill.cocktails.data.platform.ShareManager
import com.devphill.cocktails.data.platform.UrlOpener
import com.devphill.cocktails.data.platform.createUrlOpener
import com.devphill.cocktails.data.repository.AndroidCocktailRepository
import com.devphill.cocktails.domain.datasource.DatabaseCocktailDataSource
import com.devphill.cocktails.domain.datasource.LocalCocktailsDataSource
import com.devphill.cocktails.domain.repository.CocktailRepository
import com.devphill.cocktails.localization.AndroidLocaleConfiguration
import com.devphill.cocktails.localization.LocaleConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Platform-specific Koin module for Android
 * This module provides platform-specific dependencies like UserPreferencesManager and AuthManager
 */
val platformModule =
    module {

        // Database
        single<CocktailDatabase> {
            getDatabaseBuilder(androidContext()).build()
        }

        single<DatabaseCocktailDataSource> {
            DatabaseCocktailDataSourceImpl(get()) // Inject the shared database instance
        }
        single<LocalCocktailsDataSource> {
            LocalCocktailsDataSourceImpl(androidContext())
        }
        single<CocktailRepository> {
            AndroidCocktailRepository(get(), get()) // Inject both data sources
        }
        single<AuthManager> {
            createAuthManager(androidContext())
        }
        single<UrlOpener> {
            createUrlOpener(androidContext())
        }
        single<ShareManager> {
            AndroidShareManager(androidContext())
        }
        single<PushNotificationManager> {
            PushNotificationManager()
        }
        single<NotificationPermissionManager> {
            NotificationPermissionManager()
        }
        single<LocaleConfiguration> {
            AndroidLocaleConfiguration(androidContext())
        }
    }
