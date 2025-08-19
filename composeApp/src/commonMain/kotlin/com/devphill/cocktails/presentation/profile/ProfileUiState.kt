package com.devphill.cocktails.presentation.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userName: String = "",
    val userEmail: String = "",
    val experienceLevel: String = "",
    val favoriteCategory: String = "",
    val totalFavorites: Int = 0,
    val totalCocktailsMade: Int = 0,
    val isSigningOut: Boolean = false
)
