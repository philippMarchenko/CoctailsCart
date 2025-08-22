package com.devphill.cocktails.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

/**
 * Helper class to test Google Sign-In configuration
 */
object GoogleSignInTester {

    fun testConfiguration(context: Context) {
        Log.d("GoogleSignIn", "Testing Google Sign-In configuration...")

        // Test 1: Check if client ID is properly configured
        val clientId = AuthConstants.GOOGLE_WEB_CLIENT_ID
        Log.d("GoogleSignIn", "Client ID: $clientId")

        if (clientId.contains("YOUR_OAUTH_CLIENT_ID")) {
            Log.e("GoogleSignIn", "❌ Client ID not configured properly!")
        } else {
            Log.d("GoogleSignIn", "✅ Client ID configured")
        }

        // Test 2: Check if Credential Manager is available
        try {
            val credentialManager = CredentialManager.create(context)
            Log.d("GoogleSignIn", "✅ Credential Manager created successfully")
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "❌ Credential Manager failed: ${e.message}")
        }

        // Test 3: Check if Google ID option can be built
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(clientId)
                .build()
            Log.d("GoogleSignIn", "✅ Google ID option built successfully")
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "❌ Google ID option failed: ${e.message}")
        }

        Log.d("GoogleSignIn", "Configuration test completed. Check logs above for any issues.")
    }
}
