package com.devphill.cocktails.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.data.auth.AuthManager
import com.devphill.cocktails.data.auth.User
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlinx.coroutines.launch

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object GoogleSignInRequired : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val authManager: AuthManager,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(
        if (authManager.isUserSignedIn()) {
            AuthState.Authenticated(authManager.getCurrentUser()!!)
        } else {
            AuthState.Unauthenticated
        }
    )

    private suspend fun saveUserData(user: User) {
        userPreferencesManager.saveUser(user)
        userPreferencesManager.setUserLoggedIn(true)
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val result = authManager.signInWithEmailAndPassword(email, password)
                result.fold(
                    onSuccess = { user ->
                        saveUserData(user)
                        authState = AuthState.Authenticated(user)
                    },
                    onFailure = { exception ->
                        authState = AuthState.Error(exception.message ?: "Sign in failed")
                    }
                )
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signUpWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val result = authManager.createUserWithEmailAndPassword(email, password)
                result.fold(
                    onSuccess = { user ->
                        saveUserData(user)
                        authState = AuthState.Authenticated(user)
                    },
                    onFailure = { exception ->
                        authState = AuthState.Error(exception.message ?: "Sign up failed")
                    }
                )
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val result = authManager.signInWithGoogle()
                result.fold(
                    onSuccess = { user ->
                        saveUserData(user)
                        authState = AuthState.Authenticated(user)
                    },
                    onFailure = { exception ->
                        println("Google sign-in failed: ${exception.message}")
                        authState = AuthState.Error(exception.message ?: "Google sign in failed")
                    }
                )
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Google sign in failed")
            }
        }
    }

    fun signInWithGoogleToken(idToken: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val result = authManager.signInWithGoogleToken(idToken)
                result.fold(
                    onSuccess = { user ->
                        saveUserData(user)
                        authState = AuthState.Authenticated(user)
                    },
                    onFailure = { exception ->
                        authState = AuthState.Error(exception.message ?: "Google sign in with token failed")
                    }
                )
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Google sign in with token failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                authManager.signOut()
                userPreferencesManager.clearUserData()
                authState = AuthState.Unauthenticated
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Sign out failed")
            }
        }
    }

    fun resetToUnauthenticated() {
        authState = AuthState.Unauthenticated
    }
}
