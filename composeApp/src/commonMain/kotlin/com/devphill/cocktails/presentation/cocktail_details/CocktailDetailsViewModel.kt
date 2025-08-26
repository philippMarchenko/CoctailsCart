package com.devphill.cocktails.presentation.cocktail_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.domain.interactor.CocktailInteractor
import com.devphill.cocktails.domain.model.Cocktail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CocktailDetailsUiState(
    val cocktail: Cocktail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class CocktailDetailsViewModel(
    private val cocktailInteractor: CocktailInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(CocktailDetailsUiState())
    val uiState: StateFlow<CocktailDetailsUiState> = _uiState.asStateFlow()

    fun loadCocktail(cocktailId: String) {
        viewModelScope.launch {
            // Only show loading if we don't already have the cocktail data
            val shouldShowLoading = _uiState.value.cocktail?.id != cocktailId

            if (shouldShowLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }

            try {
                val cocktail = cocktailInteractor.getCocktailById(cocktailId)
                _uiState.value = _uiState.value.copy(
                    cocktail = cocktail,
                    isLoading = false,
                    error = if (cocktail == null) "Cocktail not found" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun toggleFavorite() {
        val currentCocktail = _uiState.value.cocktail ?: return

        viewModelScope.launch {
            try {
                cocktailInteractor.toggleFavorite(currentCocktail, currentCocktail.isFavorite)
                // Update the local state
                val updatedCocktail = currentCocktail.copy(isFavorite = !currentCocktail.isFavorite)
                _uiState.value = _uiState.value.copy(cocktail = updatedCocktail)
            } catch (e: Exception) {
                // Handle error - could show a snackbar or toast
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update favorite status"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}


