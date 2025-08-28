package com.devphill.cocktails.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devphill.cocktails.presentation.theme.CocktailsTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToAuth: () -> Unit,
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    modifier: Modifier = Modifier
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
        modifier = modifier
    )
}

// Preview
@Preview
@Composable
private fun ProfileMainContentPreview() {
    CocktailsTheme(useDarkTheme = true) {
        ProfileMainContent(
            uiState = ProfileUiState(
                userName = "John Doe",
                userEmail = "john.doe@example.com",
                userPhotoUrl = null,
                isLoggedIn = true,
                favoriteCategory = "Whiskey Cocktails",
                totalFavorites = 12,
                totalCocktailsMade = 45
            ),
            viewModel = koinViewModel(),
            onSignOut = { },
            onDeleteAccount = { },
            onNavigateToFavorites = { }
        )
    }
}
