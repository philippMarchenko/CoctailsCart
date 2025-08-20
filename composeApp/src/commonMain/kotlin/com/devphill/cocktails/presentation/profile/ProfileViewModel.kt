package com.devphill.cocktails.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.auth.AuthManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferencesManager: UserPreferencesManager,
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val displayName = userPreferencesManager.getUserDisplayName() ?: "Anonymous"
                val email = userPreferencesManager.getUserEmail() ?: ""
                val photoUrl = userPreferencesManager.getUserPhotoUrl()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = displayName,
                    userEmail = email,
                    userPhotoUrl = photoUrl,
                    isLoggedIn = userPreferencesManager.isUserLoggedIn()
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
                authManager.signOut()
                userPreferencesManager.clearUserData()
                _uiState.value = ProfileUiState(isSigningOut = false)
                onSignOutComplete()
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSigningOut = false,
                    errorMessage = exception.message ?: "Failed to sign out"
                )
            }
        }
    }
}
