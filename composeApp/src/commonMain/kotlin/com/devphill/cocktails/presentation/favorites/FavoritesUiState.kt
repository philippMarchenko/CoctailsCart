package com.devphill.cocktails.presentation.favorites

import com.devphill.cocktails.domain.model.Cocktail

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favorites: List<Cocktail> = emptyList(),
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)
