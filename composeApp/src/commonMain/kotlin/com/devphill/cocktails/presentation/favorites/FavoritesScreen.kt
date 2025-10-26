package com.devphill.cocktails.presentation.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cocktailscart.composeapp.generated.resources.Res
import cocktailscart.composeapp.generated.resources.discover_cocktails
import cocktailscart.composeapp.generated.resources.error_loading_favorites
import cocktailscart.composeapp.generated.resources.favorites
import cocktailscart.composeapp.generated.resources.no_favorites_yet
import cocktailscart.composeapp.generated.resources.remove_from_favorites
import cocktailscart.composeapp.generated.resources.retry
import cocktailscart.composeapp.generated.resources.start_exploring_message
import cocktailscart.composeapp.generated.resources.your_saved_cocktails
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.presentation.common.LoadingIndicator
import com.devphill.cocktails.presentation.theme.CocktailScreenTitle
import com.devphill.cocktails.presentation.theme.CocktailSectionHeader
import com.devphill.cocktails.presentation.theme.CocktailSubtitle
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    modifier: Modifier = Modifier,
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToCocktailDetails: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesContent(
        uiState = uiState,
        onRemoveFavorite = viewModel::removeFromFavorites,
        onRetry = viewModel::loadFavorites,
        onNavigateToDiscover = onNavigateToDiscover,
        onNavigateToCocktailDetails = onNavigateToCocktailDetails,
        modifier = modifier,
    )
}

@Composable
private fun FavoritesContent(
    uiState: FavoritesUiState,
    onRemoveFavorite: (Cocktail) -> Unit,
    onRetry: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onNavigateToCocktailDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        FavoritesHeader()

        Spacer(modifier = Modifier.height(24.dp))

        when {
            uiState.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            uiState.errorMessage != null -> {
                ErrorState(
                    message = uiState.errorMessage,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            uiState.isEmpty -> {
                EmptyFavoritesState(
                    onNavigateToDiscover = onNavigateToDiscover,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                )
            }

            else -> {
                FavoritesList(
                    favorites = uiState.favorites,
                    onRemoveFavorite = onRemoveFavorite,
                    onNavigateToCocktailDetails = onNavigateToCocktailDetails,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun FavoritesHeader() {
    Column {
        CocktailScreenTitle(
            text = stringResource(Res.string.favorites),
        )
        CocktailSubtitle(
            text = stringResource(Res.string.your_saved_cocktails),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun FavoritesList(
    favorites: List<Cocktail>,
    onRemoveFavorite: (Cocktail) -> Unit,
    onNavigateToCocktailDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CocktailSectionHeader(
            text = "${favorites.size} Favorite${if (favorites.size != 1) "s" else ""}",
            modifier = Modifier.padding(bottom = 12.dp),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(favorites) { cocktail ->
                FavoriteItem(
                    cocktail = cocktail,
                    onRemoveFavorite = { onRemoveFavorite(cocktail) },
                    onCocktailClick = { onNavigateToCocktailDetails(cocktail.id) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteItem(
    cocktail: Cocktail,
    onRemoveFavorite: () -> Unit,
    onCocktailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .clickable { onCocktailClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = cocktail.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = cocktail.method,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = cocktail.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${cocktail.preparationTime} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                IconButton(
                    onClick = onRemoveFavorite,
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(Res.string.remove_from_favorites),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyFavoritesState(
    onNavigateToDiscover: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        )

        Spacer(modifier = Modifier.height(24.dp))

        CocktailSectionHeader(
            text = stringResource(Res.string.no_favorites_yet),
        )

        Text(
            text = stringResource(Res.string.start_exploring_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToDiscover,
        ) {
            Text(stringResource(Res.string.discover_cocktails))
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.error_loading_favorites),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRetry) {
            Text(stringResource(Res.string.retry))
        }
    }
}
