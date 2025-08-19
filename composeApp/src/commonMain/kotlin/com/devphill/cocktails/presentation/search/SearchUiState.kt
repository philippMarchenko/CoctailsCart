package com.devphill.cocktails.presentation.search

import com.devphill.cocktails.domain.model.Cocktail

data class SearchUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val searchResults: List<Cocktail> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val errorMessage: String? = null,
    val isSearchActive: Boolean = false
)
