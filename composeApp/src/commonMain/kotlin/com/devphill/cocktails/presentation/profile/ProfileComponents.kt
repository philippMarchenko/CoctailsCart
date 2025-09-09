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
import androidx.compose.ui.unit.dp
import cocktailscart.composeapp.generated.resources.Res
import cocktailscart.composeapp.generated.resources.*
import coil3.compose.AsyncImage
import com.devphill.cocktails.presentation.common.ErrorMessage
import com.devphill.cocktails.presentation.common.LoadingIndicator
import com.devphill.cocktails.presentation.theme.CocktailBodyText
import com.devphill.cocktails.presentation.theme.ThemeSettingsDialog
import com.devphill.cocktails.presentation.theme.CocktailScreenTitle
import com.devphill.cocktails.presentation.theme.CocktailSubtitle
import com.devphill.cocktails.localization.*
import com.devphill.cocktails.presentation.theme.ThemeManager
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

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
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    val themeManager = koinInject<ThemeManager>()
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
            onLanguageClick = { showLanguageDialog = true },
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

    // Language Settings Dialog
    if (showLanguageDialog) {
        LanguageSettingsDialog(
            currentLanguage = viewModel.getCurrentLanguage(),
            onLanguageSelected = { language ->
                viewModel.saveLanguage(language)
                showLanguageDialog = false
            },
            onDismiss = {
                showLanguageDialog = false
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
        CocktailScreenTitle(
            text = stringResource(Res.string.profile)
        )

        CocktailSubtitle(
            text = stringResource(Res.string.profile_subtitle),
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
                    contentDescription = stringResource(Res.string.user_avatar),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(Res.string.default_avatar),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        CocktailSubtitle(
            text = uiState.userName,
        )

        // User Email
        CocktailBodyText(
            text = uiState.userEmail,
        )
    }
}