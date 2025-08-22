package com.devphill.cocktails.di

import com.devphill.cocktails.data.auth.AuthManager
import com.devphill.cocktails.data.auth.createAuthManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.data.preferences.createUserPreferencesManager
import com.devphill.cocktails.data.repository.CocktailRepositoryImpl
import com.devphill.cocktails.domain.repository.CocktailRepository
import org.koin.dsl.module

val platformModule = module {

    single<CocktailRepository> {
        CocktailRepositoryImpl(null) // get() will resolve the Android context
    }

    single<UserPreferencesManager> {
        createUserPreferencesManager(null) // iOS doesn't need context
    }
    single<AuthManager> {
        createAuthManager(null)
    }
}