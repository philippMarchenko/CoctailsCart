package com.devphill.cocktails.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.usecase.GetAllCocktailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

class DiscoverViewModel(
    private val getAllCocktailsUseCase: GetAllCocktailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    var lastRotatedTime = -1L // Initialize to an invalid time

    companion object {
        // Change this constant to modify the rotation period
        // 5000L = 5 seconds, 86400000L = 24 hours (1 day)
        private const val COCKTAIL_ROTATION_PERIOD_MS = 5000L
    }

    init {
        loadCocktails()
    }

    @OptIn(ExperimentalTime::class)
    private fun selectCocktailOfTheDay(cocktails: List<Cocktail>): Cocktail? {
        if (cocktails.isEmpty()) return null
        // Convert the Instant to milliseconds since the Unix epoch
        val currentTime = System.now().toEpochMilliseconds()

        if(lastRotatedTime == -1L) {
            // First time rotation, set the last rotated time
            lastRotatedTime = currentTime
            return cocktails[cocktails.indices.random()]
        } else if (currentTime - lastRotatedTime >= COCKTAIL_ROTATION_PERIOD_MS) {
            // Update the last rotated time only if the period has passed
            lastRotatedTime = currentTime
            return cocktails[cocktails.indices.random()]
        } else {
            // If not enough time has passed, return the previously selected cocktail
            return _uiState.value.cocktailOfDay
        }
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

                        // Use the separate method to select cocktail of the day
                        val cocktailOfDay = selectCocktailOfTheDay(cocktails)

                        println("🍹 Initial Cocktail of Day: ${cocktailOfDay?.title}")

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            cocktails = cocktails,
                            cocktailOfDay = cocktailOfDay,
                            errorMessage = null
                        )
                    }
            } catch (e: Exception) {
                println("❌ DiscoverViewModel: Exception caught: ${e.message}")
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

