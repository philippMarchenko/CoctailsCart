package com.devphill.cocktails

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector

import com.devphill.cocktails.presentation.auth.signin.PlatformSignInScreen
import com.devphill.cocktails.presentation.discover.DiscoverScreen
import com.devphill.cocktails.presentation.search.SearchScreen
import com.devphill.cocktails.presentation.favorites.FavoritesScreen
import com.devphill.cocktails.presentation.tutorials.TutorialsScreen
import com.devphill.cocktails.presentation.profile.ProfileScreen
import com.devphill.cocktails.presentation.splash.SplashScreen
import com.devphill.cocktails.ui.theme.CocktailsTheme
import com.devphill.cocktails.ui.theme.GlobalThemeManager
import com.devphill.cocktails.ui.theme.ThemeMode
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.tutorials.TutorialsViewModel
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

sealed class BottomNavScreen(val title: String, val icon: ImageVector) {
    object Discover : BottomNavScreen("Discover", Icons.Filled.Explore)
    object Tutorials : BottomNavScreen("Tutorials", Icons.AutoMirrored.Filled.MenuBook)
    object Search : BottomNavScreen("Search", Icons.Filled.Search)
    object Favorites : BottomNavScreen("Favorites", Icons.Filled.Star)
    object Profile : BottomNavScreen("Profile", Icons.Filled.Person)
}

enum class AppState {
    SPLASH,
    SIGN_IN,
    MAIN_APP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var appState by remember { mutableStateOf(AppState.SPLASH) }
    val userPreferencesManager: UserPreferencesManager = koinInject()
    val themeManager = remember { GlobalThemeManager.getThemeManager() }
    val currentTheme by themeManager.currentTheme.collectAsState()

    // Update status bar when theme changes
    LaunchedEffect(currentTheme) {
        // This will trigger a recomposition and update the status bar
        // The actual status bar update happens in the theme
    }

    CocktailsTheme(useDarkTheme = currentTheme == ThemeMode.SYSTEM) {
        when (appState) {
            AppState.SPLASH -> {
                SplashScreen(
                    userPreferencesManager = userPreferencesManager,
                    onNavigateToSignIn = { appState = AppState.SIGN_IN },
                    onNavigateToMain = { appState = AppState.MAIN_APP }
                )
            }
            AppState.SIGN_IN -> {
                PlatformSignInScreen(
                    onSignInSuccess = {
                        // Save login state
                        appState = AppState.MAIN_APP
                    },
                    onSkipSignIn = {
                        // For now, just continue to main app (you can add sign up screen later)
                        appState = AppState.MAIN_APP
                    }
                )
            }
            AppState.MAIN_APP -> {
                MainApp()
            }
        }
    }
}

@Composable
private fun MainApp() {
    var selectedScreen by remember { mutableStateOf<BottomNavScreen>(BottomNavScreen.Discover) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val screens = listOf(
                    BottomNavScreen.Discover,
                    BottomNavScreen.Tutorials,
                    BottomNavScreen.Search,
                    BottomNavScreen.Favorites,
                    BottomNavScreen.Profile
                )

                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedScreen) {
            BottomNavScreen.Discover -> {
                val viewModel: DiscoverViewModel = koinViewModel()
                DiscoverScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel
                )
            }
            BottomNavScreen.Tutorials -> {
                val viewModel: TutorialsViewModel = koinViewModel()
                TutorialsScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel
                )
            }
            BottomNavScreen.Search -> {
                val viewModel: SearchViewModel = koinViewModel()
                SearchScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel
                )
            }
            BottomNavScreen.Favorites -> {
                val viewModel: FavoritesViewModel = koinViewModel()
                FavoritesScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel
                )
            }
            BottomNavScreen.Profile -> {
                val viewModel: ProfileViewModel = koinViewModel()
                ProfileScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel
                )
            }
        }
    }
}
