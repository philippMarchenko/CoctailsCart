package com.devphill.cocktails

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.NavType

import com.devphill.cocktails.presentation.auth.signin.PlatformSignInScreen
import com.devphill.cocktails.presentation.auth.signup.PlatformSignUpScreen
import com.devphill.cocktails.presentation.discover.DiscoverScreen
import com.devphill.cocktails.presentation.search.SearchScreen
import com.devphill.cocktails.presentation.favorites.FavoritesScreen
import com.devphill.cocktails.presentation.tutorials.TutorialsScreen
import com.devphill.cocktails.presentation.profile.ProfileScreen
import com.devphill.cocktails.presentation.splash.SplashScreen
import com.devphill.cocktails.presentation.cocktail_details.CocktailDetailsScreenContainer
import com.devphill.cocktails.presentation.cocktail_details.CocktailDetailsViewModel
import com.devphill.cocktails.ui.theme.CocktailsTheme
import com.devphill.cocktails.ui.theme.GlobalThemeManager
import com.devphill.cocktails.ui.theme.ThemeMode
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.tutorials.TutorialsViewModel
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import com.devphill.cocktails.platform.UrlOpener
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

// Navigation routes as constants
object NavigationRoutes {
    const val DISCOVER = "discover"
    const val TUTORIALS = "tutorials"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val COCKTAIL_DETAILS = "cocktail_details/{cocktailId}"

    fun cocktailDetails(cocktailId: String) = "cocktail_details/$cocktailId"
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    object Discover : BottomNavScreen(NavigationRoutes.DISCOVER, "Discover", Icons.Filled.Explore)
    object Tutorials : BottomNavScreen(NavigationRoutes.TUTORIALS, "Tutorials", Icons.AutoMirrored.Filled.MenuBook)
    object Search : BottomNavScreen(NavigationRoutes.SEARCH, "Search", Icons.Filled.Search)
    object Favorites : BottomNavScreen(NavigationRoutes.FAVORITES, "Favorites", Icons.Filled.Star)
    object Profile : BottomNavScreen(NavigationRoutes.PROFILE, "Profile", Icons.Filled.Person)
}

enum class AppState {
    SPLASH,
    SIGN_IN,
    SIGN_UP,
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

    CocktailsTheme(useDarkTheme = currentTheme == ThemeMode.DARK) {
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
                    onNavigateToSignUp = {
                        appState = AppState.SIGN_UP
                    }
                )
            }
            AppState.SIGN_UP -> {
                PlatformSignUpScreen(
                    onSignUpSuccess = {
                        // Save login state and navigate to main app
                        appState = AppState.MAIN_APP
                    },
                    onNavigateToSignIn = {
                        appState = AppState.SIGN_IN
                    }
                )
            }
            AppState.MAIN_APP -> {
                MainApp(
                    onNavigateToAuth = {
                        appState = AppState.SIGN_IN
                    }
                )
            }
        }
    }
}

@Composable
private fun MainApp(onNavigateToAuth: () -> Unit) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Get the UrlOpener from dependency injection
    val urlOpener: UrlOpener = koinInject()

    // Helper function to handle video clicks - now opens YouTube links!
    val handleVideoClick = { videoUrl: String ->
        urlOpener.openUrl(videoUrl)
    }

    // Helper function to handle share clicks
    val handleShareClick = { cocktailTitle: String ->
        // TODO: Implement share functionality
        println("Share cocktail: $cocktailTitle")
    }

    // Define bottom navigation screens
    val bottomNavScreens = listOf(
        BottomNavScreen.Discover,
        BottomNavScreen.Tutorials,
        BottomNavScreen.Search,
        BottomNavScreen.Favorites,
        BottomNavScreen.Profile
    )

    // Check if current route is a bottom nav screen
    val isBottomNavScreen = bottomNavScreens.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            // Only show bottom bar for main screens, not for cocktail details
            if (isBottomNavScreen) {
                NavigationBar {
                    bottomNavScreens.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination to avoid building up a large stack
                                    popUpTo(NavigationRoutes.DISCOVER) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.DISCOVER,
            modifier = Modifier // Remove padding to allow edge-to-edge for detail screens
        ) {
            composable(NavigationRoutes.DISCOVER) {
                val viewModel: DiscoverViewModel = koinViewModel()
                DiscoverScreen(
                    modifier = Modifier.padding(paddingValues), // Add padding for main screen
                    viewModel = viewModel,
                    onCocktailClick = { cocktailId ->
                        navController.navigate(NavigationRoutes.cocktailDetails(cocktailId))
                    }
                )
            }

            composable(NavigationRoutes.TUTORIALS) {
                val viewModel: TutorialsViewModel = koinViewModel()
                TutorialsScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel)
            }

            composable(NavigationRoutes.SEARCH) {
                val viewModel: SearchViewModel = koinViewModel()
                SearchScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel,
                    onCocktailClick = { cocktailId ->
                        navController.navigate(NavigationRoutes.cocktailDetails(cocktailId))
                    }
                )
            }

            composable(NavigationRoutes.FAVORITES) {
                val viewModel: FavoritesViewModel = koinViewModel()
                FavoritesScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel,
                    onNavigateToDiscover = {
                        navController.navigate(NavigationRoutes.DISCOVER) {
                            popUpTo(NavigationRoutes.DISCOVER) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToCocktailDetails = { cocktailId ->
                        navController.navigate(NavigationRoutes.cocktailDetails(cocktailId))
                    }
                )
            }

            composable(NavigationRoutes.PROFILE) {
                val viewModel: ProfileViewModel = koinViewModel()
                ProfileScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel,
                    onNavigateToAuth = onNavigateToAuth
                )
            }

            composable(
                route = NavigationRoutes.COCKTAIL_DETAILS,
                arguments = listOf(navArgument("cocktailId") { type = NavType.StringType })
            ) { backStackEntry ->
                val cocktailId = checkNotNull(backStackEntry.arguments?.getString("cocktailId")) {
                    "cocktailId parameter wasn't found. Please make sure it's set!"
                }
                val viewModel: CocktailDetailsViewModel = koinViewModel()
                CocktailDetailsScreenContainer(
                    cocktailId = cocktailId,
                    onBackClick = { navController.navigateUp() },
                    onVideoClick = handleVideoClick,
                    onShareClick = handleShareClick,
                    modifier = Modifier, // No padding for edge-to-edge display
                    viewModel = viewModel
                )
            }
        }
    }
}
