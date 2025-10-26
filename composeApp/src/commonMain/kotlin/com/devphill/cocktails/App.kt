package com.devphill.cocktails

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cocktailscart.composeapp.generated.resources.Res
import cocktailscart.composeapp.generated.resources.discover
import cocktailscart.composeapp.generated.resources.favourite_drinks
import cocktailscart.composeapp.generated.resources.profile
import cocktailscart.composeapp.generated.resources.search
import com.devphill.cocktails.analytics.Analytics
import com.devphill.cocktails.data.manager.FirstLaunchManager
import com.devphill.cocktails.data.platform.UrlOpener
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import com.devphill.cocktails.localization.LocalLanguage
import com.devphill.cocktails.localization.LocalizationManager
import com.devphill.cocktails.presentation.auth.signin.PlatformSignInScreen
import com.devphill.cocktails.presentation.auth.signup.PlatformSignUpScreen
import com.devphill.cocktails.presentation.cocktailDetails.CocktailDetailsScreenContainer
import com.devphill.cocktails.presentation.cocktailDetails.CocktailDetailsViewModel
import com.devphill.cocktails.presentation.discover.DiscoverScreen
import com.devphill.cocktails.presentation.discover.DiscoverViewModel
import com.devphill.cocktails.presentation.favorites.FavoritesScreen
import com.devphill.cocktails.presentation.favorites.FavoritesViewModel
import com.devphill.cocktails.presentation.notifications.NotificationDetailsScreen
import com.devphill.cocktails.presentation.notifications.NotificationsScreen
import com.devphill.cocktails.presentation.notifications.NotificationsViewModel
import com.devphill.cocktails.presentation.profile.ProfileScreen
import com.devphill.cocktails.presentation.profile.ProfileViewModel
import com.devphill.cocktails.presentation.search.SearchScreen
import com.devphill.cocktails.presentation.search.SearchViewModel
import com.devphill.cocktails.presentation.splash.SplashScreen
import com.devphill.cocktails.presentation.theme.CocktailLabel
import com.devphill.cocktails.presentation.theme.CocktailsTheme
import com.devphill.cocktails.presentation.theme.ThemeManager
import com.devphill.cocktails.presentation.theme.ThemeMode
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
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
    const val NOTIFICATIONS = "notifications"
    const val NOTIFICATION_DETAILS = "notification_details/{notificationId}"
    const val COCKTAIL_DETAILS = "cocktail_details/{cocktailId}"

    fun cocktailDetails(cocktailId: String) = "cocktail_details/$cocktailId"

    fun notificationDetails(notificationId: String) = "notification_details/$notificationId"
}

sealed class BottomNavScreen(val route: String, val title: StringResource, val icon: ImageVector) {
    object Discover : BottomNavScreen(NavigationRoutes.DISCOVER, Res.string.discover, Icons.Filled.Explore)

    object Search : BottomNavScreen(NavigationRoutes.SEARCH, Res.string.search, Icons.Filled.Search)

    object Favorites : BottomNavScreen(NavigationRoutes.FAVORITES, Res.string.favourite_drinks, Icons.Filled.Star)

    object Profile : BottomNavScreen(NavigationRoutes.PROFILE, Res.string.profile, Icons.Filled.Person)
}

@Composable
@Preview
fun App() {
    val userPreferencesManager: UserPreferencesManager = koinInject()
    val firstLaunchManager: FirstLaunchManager = koinInject()

    val themeManager: ThemeManager = koinInject()
    val localizationManager: LocalizationManager = koinInject()

    val currentLanguage by localizationManager.currentLanguage.collectAsState()

    val currentTheme by themeManager.currentTheme.collectAsState()
    val navController = rememberNavController()

    // Check login state in a LaunchedEffect
    var isLoginCheckComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // We still check login status for the splash screen logic, but we don't store it here
        // since the splash screen handles the navigation decision
        isLoginCheckComplete = true

        // Handle first launch and show welcome notification if it's the first time
        firstLaunchManager.handleFirstLaunch()
    }

    // Don't render until login check is complete
    if (!isLoginCheckComplete) {
        return
    }

    // Always start with splash screen to show beautiful animation
    val startDestination = NavigationRoutes.SPLASH

    CompositionLocalProvider(
        LocalLanguage provides currentLanguage,
    ) {
        CocktailsTheme(useDarkTheme = currentTheme == ThemeMode.DARK) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                // Auth flow screens
                composable(NavigationRoutes.SPLASH) {
                    LaunchedEffect(Unit) { Analytics.logScreen("Splash") }
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
                        },
                    )
                }

                composable(NavigationRoutes.SIGN_IN) {
                    LaunchedEffect(Unit) { Analytics.logScreen("SignIn") }
                    PlatformSignInScreen(
                        onSignInSuccess = {
                            navController.navigate(NavigationRoutes.DISCOVER) {
                                popUpTo(NavigationRoutes.SIGN_IN) { inclusive = true }
                            }
                        },
                        onNavigateToSignUp = {
                            navController.navigate(NavigationRoutes.SIGN_UP)
                        },
                    )
                }

                composable(NavigationRoutes.SIGN_UP) {
                    LaunchedEffect(Unit) { Analytics.logScreen("SignUp") }
                    PlatformSignUpScreen(
                        onSignUpSuccess = {
                            navController.navigate(NavigationRoutes.DISCOVER) {
                                popUpTo(NavigationRoutes.SIGN_UP) { inclusive = true }
                            }
                        },
                        onNavigateToSignIn = {
                            navController.navigateUp()
                        },
                    )
                }

                // Main app screens with bottom navigation
                composable(NavigationRoutes.DISCOVER) {
                    LaunchedEffect(Unit) { Analytics.logScreen("Discover") }
                    MainApp(
                        onNavigateToAuth = {
                            navController.navigate(NavigationRoutes.SIGN_IN) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                    )
                }
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
    val bottomNavScreens =
        listOf(
            BottomNavScreen.Discover,
            BottomNavScreen.Search,
            BottomNavScreen.Favorites,
            BottomNavScreen.Profile,
        )

    // Check if current route is a bottom nav screen
    val isBottomNavScreen = bottomNavScreens.any { it.route == currentRoute }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            // Only show bottom bar for main screens, not for cocktail details
            if (isBottomNavScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    bottomNavScreens.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination to keep only one instance of each tab
                                    popUpTo(NavigationRoutes.DISCOVER) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when reselecting the same tab
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected tab
                                    restoreState = true
                                }
                            },
                            icon = { Icon(screen.icon, contentDescription = stringResource(screen.title)) },
                            label = { CocktailLabel(stringResource(screen.title)) },
                            colors =
                                NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f),
                                ),
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.DISCOVER,
            modifier = Modifier,
        ) {
            composable(NavigationRoutes.DISCOVER) {
                LaunchedEffect(Unit) { Analytics.logScreen("Discover_Main") }
                val viewModel: DiscoverViewModel = koinViewModel()
                DiscoverScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel,
                    onCocktailClick = { cocktailId ->
                        navController.navigate(NavigationRoutes.cocktailDetails(cocktailId))
                    },
                )
            }

            composable(NavigationRoutes.SEARCH) {
                LaunchedEffect(Unit) { Analytics.logScreen("Search") }
                val viewModel: SearchViewModel = koinViewModel()
                SearchScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel,
                    onCocktailClick = { cocktailId ->
                        navController.navigate(NavigationRoutes.cocktailDetails(cocktailId))
                    },
                )
            }

            composable(NavigationRoutes.FAVORITES) {
                LaunchedEffect(Unit) { Analytics.logScreen("Favorites") }
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
                    },
                )
            }

            composable(NavigationRoutes.PROFILE) {
                LaunchedEffect(Unit) { Analytics.logScreen("Profile") }
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
                    },
                    onNavigateToNotifications = {
                        navController.navigate(NavigationRoutes.NOTIFICATIONS)
                    },
                )
            }

            composable(NavigationRoutes.NOTIFICATIONS) {
                LaunchedEffect(Unit) { Analytics.logScreen("Notifications") }
                val viewModel: NotificationsViewModel = koinViewModel()
                NotificationsScreen(
                    modifier = Modifier,
                    viewModel = viewModel,
                    onBackClick = { navController.navigateUp() },
                    onNotificationClick = { notification ->
                        // Navigate to notification details screen
                        navController.navigate(NavigationRoutes.notificationDetails(notification.id))
                    },
                )
            }

            composable(
                route = NavigationRoutes.NOTIFICATION_DETAILS,
                arguments = listOf(navArgument("notificationId") { type = NavType.StringType }),
            ) { navBackStackEntry ->
                val notificationId = navBackStackEntry.savedStateHandle.get<String>("notificationId") ?: ""
                LaunchedEffect(notificationId) { Analytics.logScreen("NotificationDetails_$notificationId") }
                val viewModel: NotificationsViewModel = koinViewModel()
                NotificationDetailsScreen(
                    notificationId = notificationId,
                    modifier = Modifier,
                    viewModel = viewModel,
                    onBackClick = { navController.navigateUp() },
                    onCocktailClick = { cocktailId ->
                        navController.navigate(NavigationRoutes.cocktailDetails(cocktailId))
                    },
                    onActionClick = { actionUrl ->
                        // Handle action URL - could open in browser or handle custom actions
                        urlOpener.openUrl(actionUrl)
                    },
                )
            }

            composable(
                route = NavigationRoutes.COCKTAIL_DETAILS,
                arguments = listOf(navArgument("cocktailId") { type = NavType.StringType }),
            ) { navBackStackEntry ->
                val cocktailId = navBackStackEntry.savedStateHandle.get<String>("cocktailId") ?: ""
                LaunchedEffect(cocktailId) { Analytics.logScreen("CocktailDetails_$cocktailId") }
                val viewModel: CocktailDetailsViewModel = koinViewModel()
                CocktailDetailsScreenContainer(
                    cocktailId = cocktailId,
                    onBackClick = { navController.navigateUp() },
                    onVideoClick = handleVideoClick,
                    onShareClick = handleShareClick,
                    modifier = Modifier,
                    viewModel = viewModel,
                )
            }
        }
    }
}
