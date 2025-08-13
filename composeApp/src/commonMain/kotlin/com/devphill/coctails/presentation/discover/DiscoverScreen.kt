package com.devphill.coctails.presentation.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devphill.coctails.presentation.common.CocktailCard
import com.devphill.coctails.presentation.common.CategoryChip
import com.devphill.coctails.presentation.common.LoadingIndicator
import com.devphill.coctails.presentation.common.ErrorMessage

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

        if (uiState.categories.isNotEmpty()) {
            item {
                CategoriesSection(categories = uiState.categories)
            }
        }

        if (uiState.featuredCocktails.isNotEmpty()) {
            item {
                FeaturedSection(cocktails = uiState.featuredCocktails)
            }
        }

        if (uiState.cocktails.isNotEmpty()) {
            item {
                AllCocktailsSection(cocktails = uiState.cocktails)
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
private fun CategoriesSection(categories: List<String>) {
    Column {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category,
                    onClick = { /* Handle category click */ }
                )
            }
        }
    }
}

@Composable
private fun FeaturedSection(cocktails: List<com.devphill.coctails.domain.model.Cocktail>) {
    Column {
        Text(
            text = "Featured Cocktails",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cocktails) { cocktail ->
                CocktailCard(
                    cocktail = cocktail,
                    onClick = { /* Handle cocktail click */ },
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}

@Composable
private fun AllCocktailsSection(cocktails: List<com.devphill.coctails.domain.model.Cocktail>) {
    Column {
        Text(
            text = "All Cocktails",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        cocktails.forEach { cocktail ->
            CocktailCard(
                cocktail = cocktail,
                onClick = { /* Handle cocktail click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}
