package com.devphill.cocktails.di

import com.devphill.cocktails.data.repository.CocktailRepositoryImpl
import com.devphill.cocktails.domain.repository.CocktailRepository
import com.devphill.cocktails.domain.usecase.GetAllCocktailsUseCase
import com.devphill.cocktails.domain.usecase.SearchCocktailsUseCase
import com.devphill.cocktails.domain.usecase.GetFavoriteCocktailsUseCase
import com.devphill.cocktails.domain.usecase.ToggleFavoriteUseCase
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.tutorials.TutorialsViewModel
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.data.preferences.createUserPreferencesManager
import com.devphill.cocktails.auth.AuthManager
import com.devphill.cocktails.auth.createAuthManager

/**
 * Dependency Injection container for the Cocktails app
 * Provides all dependencies following Clean Architecture principles
 */
object DIContainer {

    private var context: Any? = null

    /**
     * Initialize the DI container with platform-specific context
     * Call this before using any dependencies
     */
    fun initialize(platformContext: Any?) {
        context = platformContext
    }

    fun provideUserPreferencesManager(): UserPreferencesManager {
        return createUserPreferencesManager(context)
    }

    // Auth Manager - using the new interface pattern
    fun provideAuthManager(): AuthManager {
        return createAuthManager(context)
    }

    // Repository - Data layer
    private val repository: CocktailRepository by lazy {
        CocktailRepositoryImpl(context)
    }

    // Use Cases - Domain layer (only the ones that actually exist)
    private val getAllCocktailsUseCase by lazy {
        GetAllCocktailsUseCase(repository)
    }

    private val searchCocktailsUseCase by lazy {
        SearchCocktailsUseCase(repository)
    }

    private val getFavoriteCocktailsUseCase by lazy {
        GetFavoriteCocktailsUseCase(repository)
    }

    private val toggleFavoriteUseCase by lazy {
        ToggleFavoriteUseCase(repository)
    }

    // ViewModels - Presentation layer (matching actual constructors)
    fun provideDiscoverViewModel(): DiscoverViewModel {
        return DiscoverViewModel(
            getAllCocktailsUseCase = getAllCocktailsUseCase
        )
    }

    fun provideSearchViewModel(): SearchViewModel {
        return SearchViewModel(
            searchCocktailsUseCase = searchCocktailsUseCase
        )
    }

    fun provideFavoritesViewModel(): FavoritesViewModel {
        return FavoritesViewModel(
            getFavoriteCocktailsUseCase = getFavoriteCocktailsUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase
        )
    }

    fun provideTutorialsViewModel(): TutorialsViewModel {
        return TutorialsViewModel()
    }

    fun provideProfileViewModel(): ProfileViewModel {
        return ProfileViewModel(
            userPreferencesManager = provideUserPreferencesManager(),
            authManager = provideAuthManager()
        )
    }
}

