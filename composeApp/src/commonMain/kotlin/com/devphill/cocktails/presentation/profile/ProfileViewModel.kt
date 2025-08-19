package com.devphill.cocktails.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.auth.AuthManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(userPreferencesManager: UserPreferencesManager, authManager: AuthManager) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                // TODO: Replace with actual data loading from repository
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = "John Doe",
                    userEmail = "john.doe@example.com",
                    experienceLevel = "Intermediate",
                    favoriteCategory = "Classic Cocktails",
                    totalFavorites = 12,
                    totalCocktailsMade = 25
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Failed to load profile data"
                )
            }
        }
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        _uiState.value = _uiState.value.copy(isSigningOut = true)

        viewModelScope.launch {
            try {
                // Clear user preferences and data
                clearUserData()

                // Navigate to login/splash screen
                onSignOutComplete()
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSigningOut = false,
                    errorMessage = exception.message ?: "Failed to sign out"
                )
            }
        }
    }

    private suspend fun clearUserData() {
        // TODO: Implement actual user data clearing
        // This would typically include:
        // - Clearing authentication tokens
        // - Clearing user preferences
        // - Clearing cached data
        // - Logging out from remote services

        // Simulate logout delay
        kotlinx.coroutines.delay(1000)
    }
}
