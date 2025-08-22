package com.devphill.cocktails.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.data.auth.AuthManager
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.delay
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
                val user = userPreferencesManager.getUser()
                println("User from prefs: $user")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = user?.displayName ?: "Guest",
                    userEmail = user?.email ?: "Not logged in",
                    userPhotoUrl = user?.photoUrl,
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
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)
            authManager.signOut()
            userPreferencesManager.clearUserData()
            onSignOutComplete()
        }
    }
}




