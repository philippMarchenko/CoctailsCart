package com.devphill.coctails.presentation.search

import com.devphill.coctails.domain.model.Cocktail

data class SearchUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val searchResults: List<Cocktail> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val errorMessage: String? = null,
    val isSearchActive: Boolean = false
)
