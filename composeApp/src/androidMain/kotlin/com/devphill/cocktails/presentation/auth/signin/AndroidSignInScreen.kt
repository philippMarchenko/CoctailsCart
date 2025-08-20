package com.devphill.cocktails.presentation.auth.signin

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devphill.cocktails.presentation.auth.AuthViewModel
import com.devphill.cocktails.presentation.auth.AuthState
import com.devphill.cocktails.presentation.auth.GoogleSignInHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AndroidSignInScreen(
    onSignInSuccess: () -> Unit,
    onSkipSignIn: () -> Unit,
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
                // This will be triggered automatically when GoogleSignInRequired state is set
                onClick()
            }
        }
        else -> {
            // Use the common SignInScreen for all other states
            SignInScreen(
                onSignInSuccess = onSignInSuccess,
                onSkipSignIn = onSkipSignIn,
                modifier = modifier,
                viewModel = viewModel
            )
        }
    }
}
