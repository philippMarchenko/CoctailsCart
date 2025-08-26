package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CocktailDetailsScreenContainer(
    cocktailId: String,
    onBackClick: () -> Unit,
    onVideoClick: (String) -> Unit,
    onShareClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CocktailDetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(cocktailId) {
        viewModel.loadCocktail(cocktailId)
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(modifier = modifier)
        }

        uiState.error != null -> {
            ErrorScreen(
                error = uiState.error!!,
                onRetry = { viewModel.loadCocktail(cocktailId) },
                onBackClick = onBackClick,
                modifier = modifier
            )
        }

        uiState.cocktail != null -> {
            CocktailDetailsScreen(
                cocktail = uiState.cocktail!!,
                onBackClick = onBackClick,
                onFavoriteClick = viewModel::toggleFavorite,
                onShareClick = { onShareClick(uiState.cocktail!!.title) },
                onVideoClick = {
                    uiState.cocktail!!.videoUrl?.let { url ->
                        onVideoClick(url)
                    }
                },
                modifier = modifier
            )
        }
    }

    // Handle error states with snackbar or similar
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // You could show a snackbar here
            // For now, we just clear the error after showing it
            viewModel.clearError()
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Loading cocktail details...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    error: String,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Oops!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = error,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick
                ) {
                    Text("Go Back")
                }

                Button(
                    onClick = onRetry
                ) {
                    Text("Try Again")
                }
            }
        }
    }
}
