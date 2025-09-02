package com.devphill.cocktails.di

import com.devphill.cocktails.data.repository.AndroidCocktailRepository
import com.devphill.cocktails.data.database.datasource.DatabaseCocktailDataSource
import com.devphill.cocktails.domain.datasource.CocktailDataSource
import com.devphill.cocktails.data.datasource.LocalCocktailsDataSourceImpl
import com.devphill.cocktails.domain.datasource.RemoteCocktailsDataSource
import com.devphill.cocktails.domain.repository.CocktailRepository
import org.koin.dsl.module

/**
 * Platform-specific Koin module for iOS
 * This module provides iOS-specific dependencies using the multiplatform Room database
 */
val platformModule = module {
    single<CocktailDataSource> {
        DatabaseCocktailDataSource(context = Unit) // iOS doesn't need context
    }
    single<RemoteCocktailsDataSource> {
        LocalCocktailsDataSourceImpl(context = null) // iOS implementation
    }
    single<CocktailRepository> {
        AndroidCocktailRepository(get(), get()) // Same repository works for both platforms
    }
}
