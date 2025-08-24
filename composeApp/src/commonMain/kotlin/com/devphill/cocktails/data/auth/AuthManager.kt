package com.devphill.cocktails.data.auth

import kotlinx.serialization.Serializable

expect fun createAuthManager(context: Any?): AuthManager

interface AuthManager {
    fun getCurrentUser(): User?
    fun isUserSignedIn(): Boolean
    suspend fun signInWithGoogle(): Result<User>
    suspend fun signInWithGoogleToken(idToken: String): Result<User>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun createUserWithEmailAndPassword(email: String, password: String, displayName: String): Result<User>
    fun signOut()
}

@Serializable
data class User(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)
