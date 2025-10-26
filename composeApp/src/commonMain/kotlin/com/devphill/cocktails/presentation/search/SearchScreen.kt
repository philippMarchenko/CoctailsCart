package com.devphill.cocktails.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cocktailscart.composeapp.generated.resources.Res
import cocktailscart.composeapp.generated.resources.clear_search
import cocktailscart.composeapp.generated.resources.find_cocktails_by_name_or_ingredients
import cocktailscart.composeapp.generated.resources.no_cocktails_found_for
import cocktailscart.composeapp.generated.resources.no_results_found
import cocktailscart.composeapp.generated.resources.recent_searches
import cocktailscart.composeapp.generated.resources.search
import cocktailscart.composeapp.generated.resources.search_by_cocktail_name_or_ingredients
import cocktailscart.composeapp.generated.resources.search_error
import cocktailscart.composeapp.generated.resources.search_placeholder
import cocktailscart.composeapp.generated.resources.search_results_with_count
import cocktailscart.composeapp.generated.resources.start_typing_to_search
import com.devphill.cocktails.presentation.common.CocktailCard
import com.devphill.cocktails.presentation.common.LoadingIndicator
import com.devphill.cocktails.presentation.theme.CocktailScreenTitle
import com.devphill.cocktails.presentation.theme.CocktailSectionHeader
import com.devphill.cocktails.presentation.theme.CocktailSubtitle
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
    onCocktailClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchContent(
        uiState = uiState,
        onQueryChange = viewModel::onSearchQueryChanged,
        onClearSearch = viewModel::clearSearch,
        onCocktailClick = onCocktailClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchContent(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onCocktailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        SearchHeader()

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            query = uiState.query,
            onQueryChange = onQueryChange,
            onClearSearch = onClearSearch,
            modifier = Modifier.fillMaxWidth(),
        )

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
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            uiState.isSearchActive && uiState.searchResults.isEmpty() && uiState.query.isNotBlank() -> {
                EmptySearchResults(
                    query = uiState.query,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            uiState.searchResults.isNotEmpty() -> {
                SearchResults(
                    results = uiState.searchResults,
                    onCocktailClick = onCocktailClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                SearchPlaceholder(
                    recentSearches = uiState.recentSearches,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun SearchHeader() {
    Column {
        CocktailScreenTitle(
            text = stringResource(Res.string.search),
        )
        CocktailSubtitle(
            text = stringResource(Res.string.find_cocktails_by_name_or_ingredients),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = stringResource(Res.string.search_placeholder),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(Res.string.search),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = onClearSearch) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(Res.string.clear_search),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
        modifier = modifier,
    )
}

@Composable
private fun SearchResults(
    results: List<com.devphill.cocktails.domain.model.Cocktail>,
    onCocktailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CocktailSectionHeader(
            text = stringResource(Res.string.search_results_with_count, results.size),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(results) { cocktail ->
                CocktailCard(
                    cocktail = cocktail,
                    onClick = { onCocktailClick(cocktail.id) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SearchPlaceholder(
    recentSearches: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.start_typing_to_search),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = stringResource(Res.string.search_by_cocktail_name_or_ingredients),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp),
        )

        if (recentSearches.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            RecentSearches(searches = recentSearches)
        }
    }
}

@Composable
private fun RecentSearches(
    searches: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.recent_searches),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        searches.take(5).forEach { search ->
            TextButton(
                onClick = { /* Handle recent search click */ },
                modifier = Modifier.padding(vertical = 2.dp),
            ) {
                Text(search)
            }
        }
    }
}

@Composable
private fun EmptySearchResults(
    query: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.no_results_found),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(Res.string.no_cocktails_found_for, query),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.search_error),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
