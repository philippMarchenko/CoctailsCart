package com.devphill.cocktails.presentation.auth.signin

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.devphill.cocktails.presentation.auth.AuthViewModel
import com.devphill.cocktails.presentation.auth.AuthState
import com.devphill.cocktails.presentation.auth.GoogleSignInHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AndroidSignInScreen(
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {
    // Handle Google sign-in state
    when (viewModel.authState) {
        is AuthState.GoogleSignInRequired -> {
            GoogleSignInHandler(
                onSignInResult = { result ->
                    result.fold(
                        onSuccess = { idToken ->
                            viewModel.signInWithGoogleToken(idToken)
                        },
                        onFailure = { exception ->
                            // Reset to unauthenticated state to show error
                            viewModel.resetToUnauthenticated()
                        }
                    )
                }
            ) { onClick ->
                // Use the common SignInScreen but with Google sign-in triggered
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onNavigateToSignUp = onNavigateToSignUp,
                    modifier = modifier,
                    viewModel = viewModel
                )
            }
        }
        else -> {
            // Use the common SignInScreen for all other states
            SignInScreen(
                onSignInSuccess = onSignInSuccess,
                onNavigateToSignUp = onNavigateToSignUp,
                modifier = modifier,
                viewModel = viewModel
            )
        }
    }
}
