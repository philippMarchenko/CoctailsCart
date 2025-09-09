package com.devphill.cocktails.di

import com.devphill.cocktails.data.manager.FirstLaunchManager
import com.devphill.cocktails.data.manager.FirstLaunchManagerImpl
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.data.preferences.UserPreferencesManagerImpl
import com.devphill.cocktails.data.repository.NotificationsRepositoryImpl
import com.devphill.cocktails.domain.interactor.CocktailInteractor
import com.devphill.cocktails.domain.interactor.CocktailInteractorImpl
import com.devphill.cocktails.domain.interactor.NotificationInteractor
import com.devphill.cocktails.domain.interactor.NotificationInteractorImpl
import com.devphill.cocktails.domain.repository.NotificationsRepository
import com.devphill.cocktails.localization.LocalizationManager
import com.devphill.cocktails.localization.LocaleConfiguration
import com.devphill.cocktails.presentation.auth.AuthViewModel
import com.devphill.cocktails.presentation.cocktailDetails.CocktailDetailsViewModel
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.notifications.NotificationsViewModel
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.theme.ThemeManager
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Common Koin module that provides shared dependencies across all platforms.
 * This module contains business logic dependencies like interactors and ViewModels.
 * Platform-specific dependencies like LocaleConfiguration are provided by platform modules.
 */
val commonModule =
    module {

        // Repositories
        single<NotificationsRepository> {
            NotificationsRepositoryImpl(get()) // Inject database
        }

        // Managers (PushNotificationManager is provided by platform-specific modules)
        // User Preferences Manager
        single<UserPreferencesManager> {
            UserPreferencesManagerImpl()
        }

        // Localization Manager
        single<LocalizationManager> {
            LocalizationManager(get(), get())
        }

        // Theme Manager
        single<ThemeManager> {
            ThemeManager(get())
        }

        single<FirstLaunchManager> {
            FirstLaunchManagerImpl(
                userPreferencesManager = get(),
                pushNotificationManager = get(),
                notificationInteractor = get(),
            )
        }

        // Interactors
        single<CocktailInteractor> {
            CocktailInteractorImpl(get())
        }

        single<NotificationInteractor> {
            NotificationInteractorImpl(get())
        }

        // ViewModels
        viewModel { AuthViewModel(get(), get()) }
        viewModel { DiscoverViewModel(get()) }
        viewModel { SearchViewModel(get()) }
        viewModel { FavoritesViewModel(get()) }
        viewModel { ProfileViewModel(get(), get(), get(), get()) }
        viewModel { CocktailDetailsViewModel(get()) }
        viewModel { NotificationsViewModel(get()) }
    }

/**
 * All application modules combined
 */
val appModules =
    listOf(
        commonModule,
    )
