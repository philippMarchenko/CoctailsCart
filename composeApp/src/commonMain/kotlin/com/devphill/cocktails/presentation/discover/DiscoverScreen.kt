package com.devphill.cocktails.presentation.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devphill.cocktails.presentation.common.CocktailImageCard
import com.devphill.cocktails.presentation.common.LoadingIndicator
import com.devphill.cocktails.presentation.common.ErrorMessage

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    userScrollEnabled = false, // Disable scrolling for the cocktails grid
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
                .aspectRatio(1.6f) // Use aspect ratio instead of fixed height for better image display
        )
    }
}