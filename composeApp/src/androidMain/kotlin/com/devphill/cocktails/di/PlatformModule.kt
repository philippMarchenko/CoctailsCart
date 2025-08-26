package com.devphill.cocktails.di

import com.devphill.cocktails.data.auth.AuthManager
import com.devphill.cocktails.data.auth.createAuthManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.data.preferences.createUserPreferencesManager
import com.devphill.cocktails.data.repository.AndroidCocktailRepository
import com.devphill.cocktails.data.database.datasource.LocalCocktailDataSource
import com.devphill.cocktails.data.datasource.CocktailsDataSource
import com.devphill.cocktails.domain.repository.CocktailRepository
import com.devphill.cocktails.platform.UrlOpener
import com.devphill.cocktails.platform.createUrlOpener
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Platform-specific Koin module for Android
 * This module provides platform-specific dependencies like UserPreferencesManager and AuthManager
 */
val platformModule = module {
    single {
        LocalCocktailDataSource(androidContext())
    }
    single {
        CocktailsDataSource(androidContext())
    }
    single<CocktailRepository> {
        AndroidCocktailRepository(get(), get()) // Inject both data sources
    }
    single<UserPreferencesManager> {
        createUserPreferencesManager(androidContext())
    }
    single<AuthManager> {
        createAuthManager(androidContext())
    }
    single<UrlOpener> {
        createUrlOpener(androidContext())
    }
}
