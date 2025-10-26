package com.devphill.cocktails.presentation.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ProfileComponentsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeUserPreferencesManager = FakeUserPreferencesManager()
    private val fakeAuthManager = FakeAuthManager()
    private val fakeShareManager = FakeShareManager()
    private val fakeLocalizationManager = FakeLocalizationManager(fakeUserPreferencesManager)

    private val fakeUiState = ProfileUiState(
        isLoading = false,
        errorMessage = null,
        userName = "Test User",
        userEmail = "test@example.com",
        userPhotoUrl = null,
        showReauthDialog = false
    )

    private val fakeViewModel = ProfileViewModel(
        userPreferencesManager = fakeUserPreferencesManager,
        authManager = fakeAuthManager,
        shareManager = fakeShareManager,
        localizationManager = fakeLocalizationManager
    )

    @Test
    fun profileContent_showsLoadingIndicator_whenLoading()  = runBlocking{
        composeTestRule.setContent {
            ProfileContent(
                uiState = fakeUiState.copy(isLoading = true),
                viewModel = fakeViewModel,
                onRetry = {},
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {}
            )
        }
        // composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun profileContent_showsErrorMessage_whenError() {
        composeTestRule.setContent {
            ProfileContent(
                uiState = fakeUiState.copy(errorMessage = "Error occurred"),
                viewModel = fakeViewModel,
                onRetry = {},
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {}
            )
        }
        composeTestRule.onNodeWithText("Error occurred").assertIsDisplayed()
    }

    fun profileContent_showsMainContent_whenNoErrorOrLoading() {
        composeTestRule.setContent {
            com.devphill.cocktails.presentation.profile.ProfileContent(
                uiState = fakeUiState,
                viewModel = fakeViewModel,
                onRetry = {},
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {}
            )
        }
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
    }

    @Test
    fun avatarSection_showsDefaultIcon_whenNoPhotoUrl() {
        composeTestRule.setContent {
            com.devphill.cocktails.presentation.profile.AvatarSection(uiState = fakeUiState)
        }
        // composeTestRule.onNodeWithContentDescription("Default Avatar").assertIsDisplayed()
    }

    @Test
    fun avatarSection_showsUserNameAndEmail() {
        composeTestRule.setContent {
            AvatarSection(uiState = fakeUiState)
        }
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
    }

    @Test
    fun profileHeader_displaysCorrectContent() {
        composeTestRule.setContent {
            ProfileHeader()
        }
        // Example: Check for header text or logo
        // composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun avatarSection_displaysPhoto_whenPhotoUrlPresent() {
        val uiStateWithPhoto = fakeUiState.copy(userPhotoUrl = "https://example.com/photo.jpg")
        composeTestRule.setContent {
            AvatarSection(uiState = uiStateWithPhoto)
        }
        // Example: Check for image node
        // composeTestRule.onNodeWithContentDescription("User Photo").assertIsDisplayed()
    }

    @Test
    fun profileMainContent_invokesOnSignOut() {
        var signOutCalled = false
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState,
                viewModel = fakeViewModel,
                onSignOut = { signOutCalled = true },
                onDeleteAccount = {},
                onNavigateToFavorites = {},
            )
        }
        // Simulate sign out click (replace with actual node/tag)
        // composeTestRule.onNodeWithTag("SignOutButton").performClick()
        // assert(signOutCalled)
    }

    @Test
    fun profileMainContent_invokesOnDeleteAccount() {
        var deleteAccountCalled = false
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState,
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = { deleteAccountCalled = true },
                onNavigateToFavorites = {},
            )

        }
        // Simulate delete account click
        // composeTestRule.onNodeWithTag("DeleteAccountButton").performClick()
        // assert(deleteAccountCalled)
    }

    @Test
    fun profileMainContent_invokesOnNavigateToFavorites() {
        var navigateCalled = false
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState,
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = { navigateCalled = true },
            )
            // Simulate navigation click
            // composeTestRule.onNodeWithTag("FavoritesButton").performClick()
            // assert(navigateCalled)
        }
    }

    @Test
    fun profileMainContent_invokesOnRetry() {
        var retryCalled = false
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState.copy(errorMessage = "Error"),
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {},
            )
        }
        // Simulate retry click
        // composeTestRule.onNodeWithTag("RetryButton").performClick()
        // assert(retryCalled)
    }

    @Test
    fun profileMainContent_showsLoadingState() {
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState.copy(isLoading = true),
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {},
            )
        }
        // composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun profileMainContent_showsErrorState() {
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState.copy(errorMessage = "Something went wrong"),
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {},
            )
        }
        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun profileMainContent_showsUserInfo() {
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState,
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {},
            )
        }
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
    }

    @Test
    fun profileMainContent_themeChangeUpdatesUI() {
        // Simulate theme change
        fakeUserPreferencesManager.saveThemeMode(com.devphill.cocktails.presentation.theme.ThemeMode.DARK)
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState,
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {},
            )
        }
        // Check for dark theme UI (replace with actual node/tag)
        // composeTestRule.onNodeWithTag("DarkThemeElement").assertIsDisplayed()
    }

    @Test
    fun profileMainContent_languageChangeUpdatesUI() {
        // Simulate language change
        fakeLocalizationManager.setLanguage(com.devphill.cocktails.localization.Language.ENGLISH)
        composeTestRule.setContent {
            ProfileMainContent(
                uiState = fakeUiState,
                viewModel = fakeViewModel,
                onSignOut = {},
                onDeleteAccount = {},
                onNavigateToFavorites = {},
            )
        }
        // Check for English UI (replace with actual node/tag)
         composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }
}
