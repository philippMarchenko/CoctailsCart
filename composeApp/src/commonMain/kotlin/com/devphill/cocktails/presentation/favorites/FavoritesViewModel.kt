package com.devphill.cocktails.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.domain.interactor.CocktailInteractor
import com.devphill.cocktails.domain.model.Cocktail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val cocktailInteractor: CocktailInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            cocktailInteractor.getFavoriteCocktails()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load favorites"
                    )
                }
                .collect { favorites ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        favorites = favorites,
                        isEmpty = favorites.isEmpty(),
                        errorMessage = null
                    )
                }
        }
    }

    fun removeFromFavorites(cocktail: Cocktail) {
        viewModelScope.launch {
            try {
                cocktailInteractor.toggleFavorite(cocktail,true)
                // Remove the cocktail from the current favorites list
                val updatedFavorites = _uiState.value.favorites.filter { it.id != cocktail.id }
                _uiState.value = _uiState.value.copy(
                    favorites = updatedFavorites,
                    isEmpty = updatedFavorites.isEmpty(),
                    errorMessage = null
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to remove favorite: ${exception.message}"
                )
            }
        }
    }
}
