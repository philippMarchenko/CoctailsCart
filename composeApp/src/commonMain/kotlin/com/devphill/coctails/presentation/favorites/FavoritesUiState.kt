package com.devphill.coctails.presentation.favorites

import com.devphill.coctails.domain.model.Cocktail

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favorites: List<Cocktail> = emptyList(),
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)
