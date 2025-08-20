package com.devphill.cocktails.di

import com.devphill.cocktails.domain.usecase.GetAllCocktailsUseCase
import com.devphill.cocktails.domain.usecase.GetFavoriteCocktailsUseCase
import com.devphill.cocktails.domain.usecase.SearchCocktailsUseCase
import com.devphill.cocktails.domain.usecase.ToggleFavoriteUseCase
import com.devphill.cocktails.presentation.auth.AuthViewModel
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.tutorials.TutorialsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Use cases module - Domain layer
 */
val useCaseModule = module {
    factory { GetAllCocktailsUseCase(get()) }
    factory { SearchCocktailsUseCase(get()) }
    factory { GetFavoriteCocktailsUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
}

/**
 * ViewModels module - Presentation layer
 */
val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { DiscoverViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavoritesViewModel(get(), get()) }
    viewModel { TutorialsViewModel() }
    viewModel { ProfileViewModel(get(), get()) }
}

/**
 * All application modules combined
 */
val appModules = listOf(
    useCaseModule,
    viewModelModule,
)

