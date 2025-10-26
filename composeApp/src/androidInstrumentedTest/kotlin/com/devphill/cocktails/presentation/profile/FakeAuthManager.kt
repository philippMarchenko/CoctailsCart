package com.devphill.cocktails.presentation.profile

import com.devphill.cocktails.data.auth.AuthManager
import com.devphill.cocktails.data.auth.User

class FakeAuthManager : AuthManager {
    private var user: User? = null

    override fun getCurrentUser(): User? = user
    override fun isUserSignedIn(): Boolean = user != null
    override suspend fun signInWithGoogle(): Result<User> = Result.success(User("fake", "Fake User", "fake@example.com", null))
    override suspend fun signInWithGoogleToken(idToken: String): Result<User> = Result.success(User("fake", "Fake User", "fake@example.com", null))
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        displayName: String,
    ): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun reauthenticateWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun reauthenticateWithGoogle(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
    // Add other required methods as needed for your tests
}

