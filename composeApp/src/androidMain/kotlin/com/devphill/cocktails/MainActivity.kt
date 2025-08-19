package com.devphill.cocktails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.devphill.cocktails.di.DIContainer
import androidx.core.view.WindowCompat
import com.devphill.cocktails.ui.theme.StatusBarController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Configure status bar for dark theme
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set status bar text/icons to light (white) for dark theme
        val statusBarController = StatusBarController(this)
        statusBarController.setStatusBarAppearance(isLight = false) // false = light icons (white)
        
        DIContainer.initialize(applicationContext)
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