package com.devphill.cocktails.presentation.auth.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformSignInScreen(
    onSignInSuccess: () -> Unit,
    onSkipSignIn: () -> Unit,
    modifier: Modifier = Modifier
)
