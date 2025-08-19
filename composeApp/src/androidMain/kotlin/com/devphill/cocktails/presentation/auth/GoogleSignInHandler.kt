package com.devphill.cocktails.presentation.auth

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import com.devphill.cocktails.auth.AuthConstants

@Composable
fun GoogleSignInHandler(
    onSignInResult: (Result<String>) -> Unit,
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val signInWithGoogle: () -> Unit = {
        if (activity == null) {
            onSignInResult(Result.failure(Exception("Activity context not available")))
        } else {
            // Use the activity's lifecycle scope to prevent restart issues
            activity.lifecycleScope.launch {
                try {
                    val credentialManager = CredentialManager.create(activity)

                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(AuthConstants.GOOGLE_WEB_CLIENT_ID)
                        .setAutoSelectEnabled(false)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val result = credentialManager.getCredential(
                        request = request,
                        context = activity,
                    )

                    val credential = result.credential
                    when (credential.type) {
                        GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                            val idToken = googleIdTokenCredential.idToken
                            onSignInResult(Result.success(idToken))
                        }
                        else -> {
                            onSignInResult(Result.failure(Exception("Unexpected type of credential")))
                        }
                    }
                } catch (e: GetCredentialException) {
                    // Handle the specific "Cannot find a matching credential" error
                    val errorMessage = when {
                        e.type == "android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL" -> {
                            if (e.message?.contains("Cannot find a matching credential") == true) {
                                "No Google accounts found on this device. Please add a Google account in Settings > Accounts."
                            } else {
                                "No Google accounts available on this device"
                            }
                        }
                        e.message?.contains("User canceled") == true -> "Sign-in canceled by user"
                        e.message?.contains("16") == true -> "User dismissed the sign-in dialog"
                        e.message?.contains("10") == true -> "Developer configuration error - check Firebase setup"
                        else -> "Google Sign-In failed: ${e.message}"
                    }
                    onSignInResult(Result.failure(Exception(errorMessage)))
                } catch (e: GoogleIdTokenParsingException) {
                    onSignInResult(Result.failure(Exception("Failed to parse Google credentials")))
                } catch (e: Exception) {
                    onSignInResult(Result.failure(Exception("Authentication error: ${e.message}")))
                }
            }
        }
    }

    content(signInWithGoogle)
}
