package com.devphill.coctails.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.coctails.domain.usecase.GetAllCocktailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val getAllCocktailsUseCase: GetAllCocktailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadCocktails()
    }

    fun loadCocktails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getAllCocktailsUseCase()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Unknown error occurred"
                    )
                }
                .collect { cocktails ->
                    val categories = cocktails.map { it.category }.distinct()
                    val featuredCocktails = cocktails.filter { it.rating >= 4.0f }.take(5)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cocktails = cocktails,
                        featuredCocktails = featuredCocktails,
                        categories = categories,
                        errorMessage = null
                    )
                }
        }
    }

    fun retryLoading() {
        loadCocktails()
    }
}
