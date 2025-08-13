package com.devphill.coctails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.TopAppBar

sealed class BottomNavScreen(val title: String, val icon: ImageVector) {
    object Discover : BottomNavScreen("Discover", Icons.Filled.Explore)
    object Tutorials : BottomNavScreen("Tutorials", Icons.Filled.MenuBook)
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
            topBar = {
                TopAppBar(
                    title = { Text(selectedScreen.title) }
                )
            },
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
                is BottomNavScreen.Discover -> DiscoverScreen()
                is BottomNavScreen.Tutorials -> TutorialsScreen()
                is BottomNavScreen.Search -> SearchScreen()
                is BottomNavScreen.Favorites -> FavoritesScreen()
                is BottomNavScreen.Profile -> ProfileScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Home Screen")
    }
}

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Search Screen")
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Profile Screen")
    }
}

@Composable
fun DiscoverScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Discover Screen")
    }
}

@Composable
fun TutorialsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Tutorials Screen")
    }
}

@Composable
fun FavoritesScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Favorites Screen")
    }
}
