package com.devphill.cocktails.presentation.auth.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformSignInScreen(
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier
) {
    AndroidSignInScreen(
        onSignInSuccess = onSignInSuccess,
        onNavigateToSignUp = onNavigateToSignUp,
        modifier = modifier
    )
}
