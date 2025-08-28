package com.devphill.cocktails.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.devphill.cocktails.presentation.common.ErrorMessage
import com.devphill.cocktails.presentation.common.LoadingIndicator
import com.devphill.cocktails.presentation.theme.GlobalThemeManager
import com.devphill.cocktails.presentation.theme.ThemeSettingsDialog

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    viewModel: ProfileViewModel,
    onRetry: () -> Unit,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToNotifications: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> LoadingIndicator(modifier = modifier)
        uiState.errorMessage != null -> ErrorMessage(
            message = uiState.errorMessage,
            onRetry = onRetry,
            modifier = modifier
        )
        else -> ProfileMainContent(
            uiState = uiState,
            viewModel = viewModel,
            onSignOut = onSignOut,
            onDeleteAccount = onDeleteAccount,
            onNavigateToFavorites = onNavigateToFavorites,
            onNavigateToNotifications = onNavigateToNotifications,
            modifier = modifier
        )
    }
}

@Composable
fun ProfileMainContent(
    uiState: ProfileUiState,
    viewModel: ProfileViewModel,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToNotifications: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    val themeManager = GlobalThemeManager.getThemeManager()
    val currentTheme by themeManager.currentTheme.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader()

        AvatarSection(uiState = uiState)

        UserInfoCard(uiState = uiState)

        QuickActionsCard(onNavigateToFavorites = onNavigateToFavorites)

        AppSettingsCard(
            onThemeClick = { showThemeDialog = true },
            onNotificationsClick = onNavigateToNotifications
        )

        AccountActionsCard(
            onSignOutClick = { showSignOutDialog = true },
            onDeleteAccountClick = { showDeleteAccountDialog = true }
        )
    }

    // Sign Out Confirmation Dialog
    if (showSignOutDialog) {
        SignOutConfirmationDialog(
            onConfirm = {
                showSignOutDialog = false
                onSignOut()
            },
            onDismiss = {
                showSignOutDialog = false
            }
        )
    }

    // Theme Settings Dialog
    if (showThemeDialog) {
        ThemeSettingsDialog(
            currentTheme = currentTheme,
            onThemeSelected = { theme ->
                themeManager.setTheme(theme)
                showThemeDialog = false
            },
            onDismiss = {
                showThemeDialog = false
            }
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteAccountDialog) {
        DeleteAccountConfirmationDialog(
            onConfirm = {
                showDeleteAccountDialog = false
                onDeleteAccount()
            },
            onDismiss = {
                showDeleteAccountDialog = false
            }
        )
    }

    // Re-authentication Dialog
    if (uiState.showReauthDialog) {
        ReAuthenticationDialog(
            userEmail = uiState.userEmail,
            onConfirm = { password ->
                viewModel.reauthenticateAndDelete(password, onDeleteAccount)
            },
            onDismiss = {
                viewModel.dismissReauthDialog()
            }
        )
    }
}

@Composable
internal fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Manage your cocktail journey",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
internal fun AvatarSection(uiState: ProfileUiState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (!uiState.userPhotoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = uiState.userPhotoUrl,
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Avatar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = uiState.userName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // User Email
        Text(
            text = uiState.userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
