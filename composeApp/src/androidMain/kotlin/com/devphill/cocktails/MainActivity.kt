package com.devphill.cocktails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.devphill.cocktails.di.appModules
import com.devphill.cocktails.di.platformModule
import com.devphill.cocktails.ui.theme.StatusBarController
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Configure window for proper keyboard handling
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Create and register status bar controller
        val statusBarController = StatusBarController(this)
      //  StatusBarControllerHolder.setController(statusBarController)
        
        // Set initial status bar appearance for dark theme
        statusBarController.setStatusBarAppearance(isLight = false) // Start with light icons for dark theme
        
        // Initialize Koin
        startKoin {
            androidContext(this@MainActivity)
            modules(appModules + platformModule)
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

