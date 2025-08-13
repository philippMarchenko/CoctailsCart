package com.devphill.coctails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.TopAppBar

import com.devphill.coctails.di.DIContainer
import com.devphill.coctails.presentation.discover.DiscoverScreen
import com.devphill.coctails.presentation.search.SearchScreen
import com.devphill.coctails.presentation.favorites.FavoritesScreen
import com.devphill.coctails.presentation.tutorials.TutorialsScreen

sealed class BottomNavScreen(val title: String, val icon: ImageVector) {
    object Discover : BottomNavScreen("Discover", Icons.Filled.Explore)
    object Tutorials : BottomNavScreen("Tutorials", Icons.AutoMirrored.Filled.MenuBook)
    object Search : BottomNavScreen("Search", Icons.Filled.Search)
    object Favorites : BottomNavScreen("Favorites", Icons.Filled.Star)
    object Profile : BottomNavScreen("Profile", Icons.Filled.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var selectedScreen by remember { mutableStateOf<BottomNavScreen>(BottomNavScreen.Discover) }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    listOf(
                        BottomNavScreen.Discover,
                        BottomNavScreen.Tutorials,
                        BottomNavScreen.Search,
                        BottomNavScreen.Favorites,
                        BottomNavScreen.Profile
                    ).forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = selectedScreen == screen,
                            onClick = { selectedScreen = screen }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (selectedScreen) {
                is BottomNavScreen.Discover -> {
                    val viewModel = remember { DIContainer.provideDiscoverViewModel() }
                    DiscoverScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                is BottomNavScreen.Tutorials -> {
                    val viewModel = remember { DIContainer.provideTutorialsViewModel() }
                    TutorialsScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                is BottomNavScreen.Search -> {
                    val viewModel = remember { DIContainer.provideSearchViewModel() }
                    SearchScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                is BottomNavScreen.Favorites -> {
                    val viewModel = remember { DIContainer.provideFavoritesViewModel() }
                    FavoritesScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                is BottomNavScreen.Profile -> ProfileScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "User profile and settings",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
