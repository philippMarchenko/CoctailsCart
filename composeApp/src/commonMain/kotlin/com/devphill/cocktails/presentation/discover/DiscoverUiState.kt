package com.devphill.cocktails.presentation.discover

import com.devphill.cocktails.domain.model.Cocktail

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val cocktails: List<Cocktail> = emptyList(),
    val cocktailOfDay: Cocktail? = null,
    val errorMessage: String? = null
)
