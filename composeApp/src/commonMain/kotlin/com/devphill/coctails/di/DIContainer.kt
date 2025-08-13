package com.devphill.coctails.di

import com.devphill.coctails.data.repository.CocktailRepositoryImpl
import com.devphill.coctails.domain.repository.CocktailRepository
import com.devphill.coctails.domain.usecase.*
import com.devphill.coctails.presentation.discover.DiscoverViewModel
import com.devphill.coctails.presentation.search.SearchViewModel
import com.devphill.coctails.presentation.favorites.FavoritesViewModel
import com.devphill.coctails.presentation.tutorials.TutorialsViewModel

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

    // Repository - Data layer
    private val repository: CocktailRepository by lazy {
        CocktailRepositoryImpl(context)
    }

    // Use Cases - Domain layer
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

    // ViewModels - Presentation layer
    fun provideDiscoverViewModel(): DiscoverViewModel {
        return DiscoverViewModel(getAllCocktailsUseCase)
    }

    fun provideSearchViewModel(): SearchViewModel {
        return SearchViewModel(searchCocktailsUseCase)
    }

    fun provideFavoritesViewModel(): FavoritesViewModel {
        return FavoritesViewModel(getFavoriteCocktailsUseCase, toggleFavoriteUseCase)
    }

    fun provideTutorialsViewModel(): TutorialsViewModel {
        return TutorialsViewModel()
    }
}
