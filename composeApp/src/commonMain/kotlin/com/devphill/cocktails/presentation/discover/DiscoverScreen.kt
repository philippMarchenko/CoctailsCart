package com.devphill.cocktails.presentation.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devphill.cocktails.data.platform.NotificationPermissionManager
import com.devphill.cocktails.presentation.common.CocktailImageCard
import com.devphill.cocktails.presentation.common.ErrorMessage
import com.devphill.cocktails.presentation.common.LoadingIndicator
import org.koin.compose.koinInject

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onCocktailClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notificationPermissionManager: NotificationPermissionManager = koinInject()

    // Request notification permission when user reaches Discovery screen
    LaunchedEffect(Unit) {
        notificationPermissionManager.requestPermissionIfNeeded()
    }

    DiscoverContent(
        uiState = uiState,
        onRetry = viewModel::retryLoading,
        onCocktailClick = onCocktailClick,
        modifier = modifier
    )
}

@Composable
private fun DiscoverContent(
    uiState: DiscoverUiState,
    onRetry: () -> Unit,
    onCocktailClick: (String) -> Unit = {},
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
                    onCocktailClick = onCocktailClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun DiscoverSuccessContent(
    uiState: DiscoverUiState,
    onCocktailClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        // Add content padding to prevent items from being cut off
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            WelcomeSection()
        }

        uiState.cocktailOfDay?.let { cocktailOfDay ->
            item {
                CocktailOfDaySection(
                    cocktail = cocktailOfDay,
                    onCocktailClick = onCocktailClick
                )
            }
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

            // Optimize cocktail grid rendering with lazy loading
            val cocktailRows = uiState.cocktails.chunked(2)
            items(
                count = cocktailRows.size,
                key = { index ->
                    // Use cocktail IDs as stable keys for better performance
                    cocktailRows[index].joinToString("-") { it.id }
                }
            ) { rowIndex ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    cocktailRows[rowIndex].forEach { cocktail ->
                        CocktailImageCard(
                            cocktail = cocktail,
                            tags = listOf(
                                cocktail.category,
                                cocktail.complexity.name
                            ),
                            onClick = { onCocktailClick(cocktail.id) },
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.8f)
                        )
                    }

                    // Add spacer for odd number of items in the last row
                    if (cocktailRows[rowIndex].size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
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
private fun CocktailOfDaySection(
    cocktail: com.devphill.cocktails.domain.model.Cocktail,
    onCocktailClick: (String) -> Unit = {}
) {
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
                cocktail.complexity.name
            ),
            onClick = { onCocktailClick(cocktail.id) },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f) // Use aspect ratio instead of fixed height for better image display
        )
    }
}
