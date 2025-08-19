package com.devphill.cocktails.presentation.auth.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformSignInScreen(
    onSignInSuccess: () -> Unit,
    onSkipSignIn: () -> Unit,
    modifier: Modifier
) {
    SignInScreen(
        onSignInSuccess = onSignInSuccess,
        onSkipSignIn = onSkipSignIn,
        modifier = modifier
    )
}
