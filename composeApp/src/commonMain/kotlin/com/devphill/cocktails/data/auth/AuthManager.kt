package com.devphill.cocktails.data.auth

import kotlinx.serialization.Serializable

/**
 * Creates a platform-specific AuthManager instance.
 * Factory function that provides the appropriate authentication implementation for each platform.
 *
 * @param context Platform-specific context (Android Context, iOS context, etc.)
 * @return Platform-specific AuthManager implementation
 */
expect fun createAuthManager(context: Any?): AuthManager

/**
 * Authentication manager interface for handling user authentication operations.
 * Provides unified authentication API across different platforms supporting various sign-in methods
 * including Google OAuth, email/password, and account management operations.
 */
interface AuthManager {
    /**
     * Retrieves the currently authenticated user.
     * @return Current user if authenticated, null if no user is signed in
     */
    fun getCurrentUser(): User?

    /**
     * Checks if a user is currently signed in to the application.
     * @return true if user is authenticated, false otherwise
     */
    fun isUserSignedIn(): Boolean

    /**
     * Initiates Google OAuth sign-in flow.
     * Launches platform-specific Google sign-in UI and handles the authentication process.
     * @return Result containing User on successful authentication or error on failure
     */
    suspend fun signInWithGoogle(): Result<User>

    /**
     * Authenticates user using a Google ID token.
     * Used for server-side authentication or when ID token is obtained externally.
     * @param idToken The Google ID token for authentication
     * @return Result containing User on successful authentication or error on failure
     */
    suspend fun signInWithGoogleToken(idToken: String): Result<User>

    /**
     * Authenticates user with email and password credentials.
     * @param email User's email address
     * @param password User's password
     * @return Result containing User on successful authentication or error on failure
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>

    /**
     * Creates a new user account with email, password, and display name.
     * Registers a new user and automatically signs them in upon successful creation.
     * @param email New user's email address
     * @param password New user's password
     * @param displayName New user's display name
     * @return Result containing User on successful account creation or error on failure
     */
    suspend fun createUserWithEmailAndPassword(email: String, password: String, displayName: String): Result<User>

    /**
     * Permanently deletes the current user's account.
     * Requires user to be recently authenticated for security purposes.
     * @return Result indicating success or error details on failure
     */
    suspend fun deleteAccount(): Result<Unit>

    /**
     * Re-authenticates the current user with email and password.
     * Required for sensitive operations like account deletion or password changes.
     * @param email User's email address
     * @param password User's current password
     * @return Result indicating success or error details on failure
     */
    suspend fun reauthenticateWithEmailAndPassword(email: String, password: String): Result<Unit>

    /**
     * Re-authenticates the current user using Google OAuth.
     * Required for sensitive operations when user originally signed in with Google.
     * @return Result indicating success or error details on failure
     */
    suspend fun reauthenticateWithGoogle(): Result<Unit>

    /**
     * Signs out the current user from the application.
     * Clears all authentication tokens and user session data.
     */
    fun signOut()
}

/**
 * Represents an authenticated user with their profile information.
 * Serializable data class containing essential user details for the application.
 *
 * @property uid Unique user identifier from authentication provider
 * @property email User's email address, null if not provided by auth provider
 * @property displayName User's display name, null if not set
 * @property photoUrl URL to user's profile photo, null if not available
 */
@Serializable
data class User(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)
