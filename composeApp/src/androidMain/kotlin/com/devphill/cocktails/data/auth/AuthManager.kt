package com.devphill.cocktails.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AndroidAuthManager(private val context: Context) : AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    companion object {
        private const val WEB_CLIENT_ID = AuthConstants.GOOGLE_WEB_CLIENT_ID
    }

    override fun getCurrentUser(): User? {
        return auth.currentUser?.toUser()
    }

    override fun isUserSignedIn(): Boolean = auth.currentUser != null

    override suspend fun signInWithGoogle(): Result<User> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            handleSignIn(result)
        } catch (e: GetCredentialException) {
            Result.failure(Exception("Google Sign-In failed: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Authentication error: ${e.message}"))
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Result<User> {
        val credential = result.credential

        return when (credential.type) {
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    signInWithGoogleToken(googleIdTokenCredential.idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    Result.failure(e)
                }
            }
            else -> {
                Result.failure(Exception("Unexpected type of credential"))
            }
        }
    }

    override suspend fun signInWithGoogleToken(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            result.user?.let {
                Result.success(it.toUser())
            } ?: Result.failure(Exception("Sign in failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.success(it.toUser())
            } ?: Result.failure(Exception("Sign in failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String, displayName: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                // Update the user's display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()

                // Return the user with updated profile
                Result.success(firebaseUser.toUser())
            } ?: Result.failure(Exception("Account creation failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                currentUser.delete().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No user is currently signed in"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete account: ${e.message}"))
        }
    }

    override suspend fun reauthenticateWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                return Result.failure(Exception("No user is currently signed in"))
            }

            // Re-authenticate with email and password
            val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password)
            currentUser.reauthenticate(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Re-authentication failed: ${e.message}"))
        }
    }

    override suspend fun reauthenticateWithGoogle(): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                return Result.failure(Exception("No user is currently signed in"))
            }

            // Get Google credentials using the same flow as sign in
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            when (credential.type) {
                GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    currentUser.reauthenticate(authCredential).await()
                    Result.success(Unit)
                }
                else -> {
                    Result.failure(Exception("Unexpected type of credential"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Re-authentication failed: ${e.message}"))
        }
    }

    override fun signOut() {
        auth.signOut()
        // Note: With Credential Manager, there's no need to explicitly sign out from Google
        // The system handles credential management automatically
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            uid = uid,
            email = email,
            displayName = displayName,
            photoUrl = photoUrl?.toString()
        )
    }
}
