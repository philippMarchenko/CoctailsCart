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

    private var isDeletingAccount = false // Add flag to prevent duplicate deletion attempts

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val user = userPreferencesManager.getUser()

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

    fun deleteAccount(onDeleteComplete: () -> Unit) {
        if (isDeletingAccount) {
            return
        }

        isDeletingAccount = true
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val result = authManager.deleteAccount()

                if (result.isSuccess) {
                    userPreferencesManager.clearUserData()
                    isDeletingAccount = false
                    onDeleteComplete()
                } else {
                    val errorMessage = result.exceptionOrNull()?.message

                    val needsReauth = errorMessage?.let { msg ->
                        msg.contains("requires recent authentication", ignoreCase = true) ||
                        msg.contains("sensitive", ignoreCase = true) ||
                        msg.contains("reauthenticate", ignoreCase = true) ||
                        msg.contains("recent", ignoreCase = true) ||
                        msg.contains("authentication", ignoreCase = true) ||
                        msg.contains("sign in again", ignoreCase = true)
                    } ?: false

                    if (needsReauth) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            showReauthDialog = true
                        )
                    } else {
                        isDeletingAccount = false
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = errorMessage ?: "Failed to delete account. Please try again."
                        )
                    }
                }
            } catch (exception: Exception) {
                isDeletingAccount = false
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Failed to delete account"
                )
            }
        }
    }

    fun reauthenticateAndDelete(password: String, onDeleteComplete: () -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true, showReauthDialog = false)

        viewModelScope.launch {
            try {
                val currentUser = authManager.getCurrentUser()
                if (currentUser?.email == null) {
                    isDeletingAccount = false
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Unable to verify user identity. Please try signing in again."
                    )
                    return@launch
                }

                // Re-authenticate with email and password
                val reauthResult = authManager.reauthenticateWithEmailAndPassword(currentUser.email, password)

                if (reauthResult.isSuccess) {
                    // Now try to delete the account
                    val deleteResult = authManager.deleteAccount()

                    // Account deletion is considered successful even if we get certain errors
                    // because Firebase might sign out the user immediately after deletion
                    val isDeleteSuccessful = deleteResult.isSuccess ||
                        deleteResult.exceptionOrNull()?.message?.contains("No user is currently signed in") == true

                    if (isDeleteSuccessful) {
                        // Clear user data regardless of the exact result
                        userPreferencesManager.clearUserData()

                        // Sign out to ensure clean state
                        authManager.signOut()

                        // Reset the flag after successful completion
                        isDeletingAccount = false

                        onDeleteComplete()
                    } else {
                        isDeletingAccount = false
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Failed to delete account after re-authentication: ${deleteResult.exceptionOrNull()?.message}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Re-authentication failed. Please check your password and try again.",
                        showReauthDialog = true // Show dialog again for retry
                    )
                    // Don't reset isDeletingAccount here - allow retry
                }
            } catch (exception: Exception) {
                isDeletingAccount = false
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to delete account: ${exception.message}",
                    showReauthDialog = true // Show dialog again for retry
                )
            }
        }
    }

    fun dismissReauthDialog() {
        _uiState.value = _uiState.value.copy(showReauthDialog = false)
    }
}
