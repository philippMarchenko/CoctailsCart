package com.devphill.coctails.presentation.discover

import com.devphill.coctails.domain.model.Cocktail

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val cocktails: List<Cocktail> = emptyList(),
    val featuredCocktails: List<Cocktail> = emptyList(),
    val categories: List<String> = emptyList(),
    val errorMessage: String? = null
)
