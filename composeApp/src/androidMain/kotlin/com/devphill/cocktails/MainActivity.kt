package com.devphill.cocktails

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.devphill.cocktails.data.platform.NotificationPermissionManager
import com.devphill.cocktails.di.appModules
import com.devphill.cocktails.di.platformModule
import com.devphill.cocktails.presentation.theme.StatusBarController
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle permission result - this is intentionally simple
        println("Notification permission granted: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Configure window for proper keyboard handling
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Create and register status bar controller
        val statusBarController = StatusBarController(this)

        // Set initial status bar appearance for dark theme
        statusBarController.setStatusBarAppearance(isLight = false) // Start with light icons for dark theme
        
        // Initialize Koin
        startKoin {
            androidContext(this@MainActivity)
            modules(appModules + platformModule)
        }

        // Register this activity with the NotificationPermissionManager
        NotificationPermissionManager.setMainActivity(this)

        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear the activity reference when destroyed
        NotificationPermissionManager.clearMainActivity()
    }

    fun requestNotificationPermissionIfNeeded() {
        println("Checking notification permission... SDK: ${Build.VERSION.SDK_INT}, Required: ${Build.VERSION_CODES.TIRAMISU}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            println("Notification permission status: $hasPermission")

            if (!hasPermission) {
                println("Requesting notification permission...")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                println("Notification permission already granted")
            }
        } else {
            println("Android version < 13, notification permission not required")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
