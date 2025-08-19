package com.devphill.cocktails.auth

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
import kotlinx.coroutines.tasks.await

class AndroidAuthManager(private val context: Context) : AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    companion object {
        // Web client ID from google-services.json (client_type: 3)
        private const val WEB_CLIENT_ID = AuthConstants.GOOGLE_WEB_CLIENT_ID
    }

    override fun getCurrentUser(): User? {
        return auth.currentUser?.toUser()
    }

    override fun isUserSignedIn(): Boolean = auth.currentUser != null

    override suspend fun signInWithGoogle(): Result<User> {
        return try {
            // For Credential Manager API, we need to return a special result
            // that tells the UI to handle the credential request
            Result.failure(Exception("GOOGLE_SIGNIN_REQUIRED"))
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

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.success(it.toUser())
            } ?: Result.failure(Exception("Account creation failed"))
        } catch (e: Exception) {
            Result.failure(e)
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
