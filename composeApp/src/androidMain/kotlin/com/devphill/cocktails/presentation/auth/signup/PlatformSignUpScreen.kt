package com.devphill.cocktails.presentation.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformSignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier
) {
    SignUpScreen(
        onSignUpSuccess = onSignUpSuccess,
        onNavigateToSignIn = onNavigateToSignIn,
        modifier = modifier
    )
}
