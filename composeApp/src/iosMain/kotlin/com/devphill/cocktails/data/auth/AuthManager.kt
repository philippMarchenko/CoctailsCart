package com.devphill.cocktails.data.auth

actual fun createAuthManager(context: Any?): AuthManager {
    return IosAuthManager()
}

// iOS implementation - for now just a stub
// You can implement Firebase iOS SDK integration here later
class IosAuthManager : AuthManager {
    override fun getCurrentUser(): User? = null

    override fun isUserSignedIn(): Boolean = false

    override suspend fun signInWithGoogle(): Result<User> {
        return Result.failure(Exception("Not implemented on iOS yet"))
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return Result.failure(Exception("Not implemented on iOS yet"))
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String, displayName: String): Result<User> {
        return Result.failure(Exception("Not implemented on iOS yet"))
    }

    override suspend fun signInWithGoogleToken(idToken: String): Result<User> {
        return Result.failure(Exception("Not implemented on iOS yet"))
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return Result.failure(Exception("Not implemented on iOS yet"))
    }

    override fun signOut() {
        // No-op for now
    }
}
