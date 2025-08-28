package com.devphill.cocktails

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.data.platform.UrlOpener
import com.devphill.cocktails.presentation.auth.signin.PlatformSignInScreen
import com.devphill.cocktails.presentation.auth.signup.PlatformSignUpScreen
import com.devphill.cocktails.presentation.cocktail_details.CocktailDetailsScreenContainer
import com.devphill.cocktails.presentation.cocktail_details.CocktailDetailsViewModel
import com.devphill.cocktails.presentation.discover.DiscoverScreen
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesScreen
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.profile.ProfileScreen
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import com.devphill.cocktails.presentation.search.SearchScreen
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.splash.SplashScreen
import com.devphill.cocktails.presentation.theme.CocktailsTheme
import com.devphill.cocktails.presentation.theme.GlobalThemeManager
import com.devphill.cocktails.presentation.theme.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

// Navigation routes as constants
object NavigationRoutes {
    const val SPLASH = "splash"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"
    const val DISCOVER = "discover"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val COCKTAIL_DETAILS = "cocktail_details/{cocktailId}"

    fun cocktailDetails(cocktailId: String) = "cocktail_details/$cocktailId"
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    object Discover : BottomNavScreen(NavigationRoutes.DISCOVER, "Discover", Icons.Filled.Explore)
    object Search : BottomNavScreen(NavigationRoutes.SEARCH, "Search", Icons.Filled.Search)
    object Favorites : BottomNavScreen(NavigationRoutes.FAVORITES, "Favorites", Icons.Filled.Star)
    object Profile : BottomNavScreen(NavigationRoutes.PROFILE, "Profile", Icons.Filled.Person)
}

@Composable
@Preview
fun App() {
    val userPreferencesManager: UserPreferencesManager = koinInject()
    val themeManager = remember { GlobalThemeManager.getThemeManager() }
    val currentTheme by themeManager.currentTheme.collectAsState()
    val navController = rememberNavController()

    // Check login state in a LaunchedEffect
    var isLoggedIn by remember { mutableStateOf(false) }
    var isLoginCheckComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoggedIn = userPreferencesManager.isUserLoggedIn()
        isLoginCheckComplete = true
    }

    // Update status bar when theme changes
    LaunchedEffect(currentTheme) {
        // This will trigger a recomposition and update the status bar
        // The actual status bar update happens in the theme
    }

    // Don't render until login check is complete
    if (!isLoginCheckComplete) {
        return
    }

    // Determine initial destination based on user login state
    val startDestination = if (isLoggedIn) {
        NavigationRoutes.DISCOVER
    } else {
        NavigationRoutes.SPLASH
    }

    CocktailsTheme(useDarkTheme = currentTheme == ThemeMode.DARK) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            // Auth flow screens
            composable(NavigationRoutes.SPLASH) {
                SplashScreen(
                    userPreferencesManager = userPreferencesManager,
                    onNavigateToSignIn = {
                        navController.navigate(NavigationRoutes.SIGN_IN) {
                            popUpTo(NavigationRoutes.SPLASH) { inclusive = true }
                        }
                    },
                    onNavigateToMain = {
                        navController.navigate(NavigationRoutes.DISCOVER) {
                            popUpTo(NavigationRoutes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavigationRoutes.SIGN_IN) {
                PlatformSignInScreen(
                    onSignInSuccess = {
                        navController.navigate(NavigationRoutes.DISCOVER) {
                            popUpTo(NavigationRoutes.SIGN_IN) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate(NavigationRoutes.SIGN_UP)
                    }
                )
            }

            composable(NavigationRoutes.SIGN_UP) {
                PlatformSignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(NavigationRoutes.DISCOVER) {
                            popUpTo(NavigationRoutes.SIGN_UP) { inclusive = true }
                        }
                    },
                    onNavigateToSignIn = {
                        navController.navigateUp()
                    }
                )
            }

            // Main app screens with bottom navigation
            composable(NavigationRoutes.DISCOVER) {
                MainApp(
                    onNavigateToAuth = {
                        navController.navigate(NavigationRoutes.SIGN_IN) {
                            popUpTo(0) { inclusive = true }
                        }
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
                                    // Pop up to the current destination to avoid building up a large stack
                                    popUpTo(screen.route) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
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
                    onNavigateToAuth = onNavigateToAuth,
                    onNavigateToFavorites = {
                        navController.navigate(NavigationRoutes.FAVORITES) {
                            popUpTo(NavigationRoutes.PROFILE) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(
                route = NavigationRoutes.COCKTAIL_DETAILS,
                arguments = listOf(navArgument("cocktailId") { type = NavType.StringType })
            ) { backStackEntry ->
                val cocktailId = backStackEntry.arguments?.getString("cocktailId") ?: ""
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
