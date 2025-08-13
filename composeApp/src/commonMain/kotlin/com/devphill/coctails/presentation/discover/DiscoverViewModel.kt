package com.devphill.coctails.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.coctails.domain.usecase.GetAllCocktailsUseCase
import kotlinx.coroutines.delay
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
        println("🔍 DiscoverViewModel: Starting to load cocktails...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                getAllCocktailsUseCase()
                    .catch { exception ->
                        println("❌ DiscoverViewModel: Error caught: ${exception.message}")
                        exception.printStackTrace()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Unknown error occurred"
                        )
                    }
                    .collect { cocktails ->
                        println("📊 DiscoverViewModel: Received ${cocktails.size} cocktails")
                        cocktails.forEach { cocktail ->
                            println("🍹 Cocktail: ${cocktail.title} - ${cocktail.category}")
                        }

                        val categories = cocktails.map { it.category }.distinct()
                        val featuredCocktails = cocktails.filter { it.rating >= 4.0f }.take(5)

                        println("📂 Categories found: $categories")
                        println("⭐ Featured cocktails: ${featuredCocktails.size}")

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            cocktails = cocktails,
                            featuredCocktails = featuredCocktails,
                            categories = categories,
                            errorMessage = null
                        )
                    }
            } catch (e: Exception) {
                println("❌ DiscoverViewModel: Exception in loadCocktails: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun retryLoading() {
        loadCocktails()
    }
}
