package com.devphill.cocktails.di

import com.devphill.cocktails.domain.interactor.CocktailInteractor
import com.devphill.cocktails.presentation.auth.AuthViewModel
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.tutorials.TutorialsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Common Koin module that provides shared dependencies across all platforms.
 * This module contains business logic dependencies like interactors and ViewModels.
 */
val commonModule = module {

    // Interactors
    single {
        CocktailInteractor(get())
    }

    // ViewModels
    viewModel { AuthViewModel(get(), get()) }
    viewModel { DiscoverViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { TutorialsViewModel() }
    viewModel { ProfileViewModel(get(), get()) }
}

/**
 * All application modules combined
 */
val appModules = listOf(
    commonModule
)
