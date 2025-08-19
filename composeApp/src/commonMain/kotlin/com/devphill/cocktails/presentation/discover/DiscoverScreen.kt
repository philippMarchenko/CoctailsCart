package com.devphill.cocktails.presentation.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devphill.cocktails.presentation.common.CocktailCard
import com.devphill.cocktails.presentation.common.CocktailImageCard
import com.devphill.cocktails.presentation.common.LoadingIndicator
import com.devphill.cocktails.presentation.common.ErrorMessage
import com.devphill.cocktails.ui.theme.GlobalThemeManager
import com.devphill.cocktails.ui.theme.ThemeMode
import com.devphill.cocktails.ui.theme.ThemeSettingsDialog

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverContent(
        uiState = uiState,
        onRetry = viewModel::retryLoading,
        modifier = modifier
    )
}

@Composable
private fun DiscoverContent(
    uiState: DiscoverUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    val themeManager = remember { GlobalThemeManager.getThemeManager() }
    val currentTheme by themeManager.currentTheme.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DiscoverTopBar(
            onThemeClick = { showThemeDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))
        when {
            uiState.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            uiState.errorMessage != null -> {
                ErrorMessage(
                    message = uiState.errorMessage,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            else -> {
                DiscoverSuccessContent(
                    uiState = uiState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Theme Settings Dialog
    if (showThemeDialog) {
        ThemeSettingsDialog(
            currentTheme = currentTheme,
            onThemeSelected = { theme ->
                themeManager.setTheme(theme)
                showThemeDialog = false
            },
            onDismiss = {
                showThemeDialog = false
            }
        )
    }
}

@Composable
private fun DiscoverSuccessContent(
    uiState: DiscoverUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            WelcomeSection()
        }

        uiState.cocktailOfDay?.let { cocktailOfDay ->
            item { CocktailOfDaySection(cocktail = cocktailOfDay) }
        }

        if (uiState.cocktails.isNotEmpty()) {
            item {
                Text(
                    text = "All Cocktails",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 800.dp) // Prevent infinite height
                ) {
                    items(uiState.cocktails.size) { index ->
                        val cocktail = uiState.cocktails[index]
                        CocktailImageCard(
                            cocktail = cocktail,
                            tags = listOf(cocktail.category) + cocktail.ingredients.take(2),
                            onClick = { /* Handle cocktail click */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.65f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Find your perfect cocktail",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun CocktailOfDaySection(cocktail: com.devphill.cocktails.domain.model.Cocktail) {
    Column {
        Text(
            text = "Cocktail of Day",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        CocktailImageCard(
            cocktail = cocktail,
            tags = listOf(
                cocktail.category,
                cocktail.alcoholStrength.name,
                cocktail.complexity.name
            ),
            onClick = { /* Handle cocktail click */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp) // Fixed height for better appearance
        )
    }
}

@Composable
private fun AllCocktailsSection(cocktails: List<com.devphill.cocktails.domain.model.Cocktail>) {
    // This function is no longer needed as we moved the grid logic above
}

@Composable
private fun DiscoverTopBar(
    onThemeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* menu */ }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }
        Text(
            text = "All drinks",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = { /* search */ }) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
        IconButton(onClick = onThemeClick) {
            Icon(Icons.Default.Palette, contentDescription = "Theme")
        }
    }
}
