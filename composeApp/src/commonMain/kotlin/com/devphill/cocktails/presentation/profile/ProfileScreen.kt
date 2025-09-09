package com.devphill.cocktails.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToAuth: () -> Unit,
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        uiState = uiState,
        viewModel = viewModel,
        onRetry = viewModel::loadProfileData,
        onSignOut = { viewModel.signOut(onNavigateToAuth) },
        onDeleteAccount = { viewModel.deleteAccount(onNavigateToAuth) },
        onNavigateToFavorites = onNavigateToFavorites,
        onNavigateToNotifications = onNavigateToNotifications,
        modifier = modifier,
    )
}
