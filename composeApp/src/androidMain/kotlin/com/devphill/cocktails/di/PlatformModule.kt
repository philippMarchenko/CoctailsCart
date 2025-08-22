package com.devphill.cocktails.di

import com.devphill.cocktails.data.auth.AuthManager
import com.devphill.cocktails.data.auth.createAuthManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.data.preferences.createUserPreferencesManager
import com.devphill.cocktails.data.repository.CocktailRepositoryImpl
import com.devphill.cocktails.domain.repository.CocktailRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Platform-specific Koin module for Android
 * This module provides platform-specific dependencies like UserPreferencesManager and AuthManager
 */
val platformModule = module {
    single<CocktailRepository> {
        CocktailRepositoryImpl(androidContext())
    }
    single<UserPreferencesManager> {
        createUserPreferencesManager(androidContext())
    }
    single<AuthManager> {
        createAuthManager(androidContext())
    }
}