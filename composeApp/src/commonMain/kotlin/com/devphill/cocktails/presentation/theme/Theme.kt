package com.devphill.cocktails.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Typography
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

// Cocktail-themed color palette inspired by the splash screen
object CocktailColors {
    // Primary colors - Deep luxury navy and gold
    val DeepNavy = Color(0xFF1A1A2E)
    val RoyalBlue = Color(0xFF16213E)
    val MidnightBlue = Color(0xFF0F0C29)
    val LuxuryPurple = Color(0xFF2D1B69)

    // Accent colors - Premium cocktail inspired
    val GoldenHour = Color(0xFFFFD700)
    val WarmOrange = Color(0xFFFFA500)
    val CocktailRed = Color(0xFFFF6B6B)
    val MintGreen = Color(0xFF4ECDC4)
    val OliveGreen = Color(0xFF90EE90)

    // Sophisticated text colors - Made significantly lighter
    val PlatinumText = Color(0xFFE8E8F0) // Much lighter platinum
    val SilverText = Color(0xFFD0D0E0)   // Much lighter silver
    val PureWhite = Color(0xFFFFFFFF)
    val WarmWhite = Color(0xFFFFFDFA)    // Even warmer white

    // Surface colors
    val GlassSurface = Color(0xFF252545)
    val CardSurface = Color(0xFF2A2A4A)
    val OverlaySurface = Color(0xFF1F1F3F)
}

// Beautiful gradient backgrounds for different app sections
object CocktailGradients {
    // Unified background gradients for all screens - much more visible and elegant
    val UnifiedDark = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A2E), // Your splash screen deep navy
            Color(0xFF2D1B69), // Your splash screen luxury purple
            Color(0xFF16213E)  // Your splash screen royal blue
        )
    )

    val UnifiedLight = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8F6FF), // Light purple tint
            Color(0xFFEADDFF), // Soft purple
            Color(0xFFE6F7F6)  // Light mint tint
        )
    )
}

// Dark theme - Primary theme matching splash screen
private val DarkCocktailColors: ColorScheme = darkColorScheme(
    primary = CocktailColors.GoldenHour,
    onPrimary = CocktailColors.DeepNavy,
    primaryContainer = CocktailColors.LuxuryPurple,
    onPrimaryContainer = CocktailColors.PlatinumText,

    secondary = CocktailColors.CocktailRed,
    onSecondary = CocktailColors.PureWhite,
    secondaryContainer = CocktailColors.OverlaySurface,
    onSecondaryContainer = CocktailColors.PlatinumText,

    tertiary = CocktailColors.MintGreen,
    onTertiary = CocktailColors.DeepNavy,
    tertiaryContainer = CocktailColors.CardSurface,
    onTertiaryContainer = CocktailColors.PlatinumText,

    background = CocktailColors.DeepNavy,
    onBackground = CocktailColors.PlatinumText, // Light text on dark background
    surface = CocktailColors.GlassSurface,
    onSurface = CocktailColors.WarmWhite, // Light text on surfaces
    surfaceVariant = CocktailColors.CardSurface,
    onSurfaceVariant = CocktailColors.PlatinumText, // Fixed: Light text for titles
    outline = CocktailColors.LuxuryPurple.copy(alpha = 0.5f),

    surfaceBright = CocktailColors.CardSurface,
    surfaceTint = CocktailColors.GoldenHour,
    inversePrimary = CocktailColors.RoyalBlue,
    inverseSurface = CocktailColors.WarmWhite,
    inverseOnSurface = CocktailColors.DeepNavy,

    // Error colors with cocktail theme
    error = CocktailColors.CocktailRed,
    onError = CocktailColors.PureWhite,
    errorContainer = Color(0xFF4D1A1A),
    onErrorContainer = Color(0xFFFFB4B4),
)

private val LightCocktailColors: ColorScheme = lightColorScheme(
    primary = CocktailColors.GoldenHour,
    onPrimary = CocktailColors.DeepNavy,
    primaryContainer = Color(0xFFF5F3FF),
    onPrimaryContainer = CocktailColors.DeepNavy,

    secondary = CocktailColors.WarmOrange,
    onSecondary = CocktailColors.PureWhite,
    secondaryContainer = Color(0xFFFFF4E6),
    onSecondaryContainer = CocktailColors.DeepNavy,

    tertiary = CocktailColors.MintGreen,
    onTertiary = CocktailColors.PureWhite,
    tertiaryContainer = Color(0xFFE6F7F6),
    onTertiaryContainer = CocktailColors.DeepNavy,

    background = CocktailColors.WarmWhite,
    onBackground = CocktailColors.DeepNavy,
    surface = CocktailColors.PureWhite,
    onSurface = CocktailColors.DeepNavy,
    surfaceVariant = Color(0xFFF8F6FF),
    onSurfaceVariant = CocktailColors.RoyalBlue,
    outline = CocktailColors.LuxuryPurple.copy(alpha = 0.3f),

    surfaceBright = CocktailColors.PureWhite,
    surfaceTint = CocktailColors.GoldenHour.copy(alpha = 0.1f),
    inversePrimary = CocktailColors.GoldenHour,
    inverseSurface = CocktailColors.DeepNavy,
    inverseOnSurface = CocktailColors.PlatinumText,

    // Error colors
    error = Color(0xFFD32F2F),
    onError = CocktailColors.PureWhite,
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFF5D1A1A),
)

// Enhanced shapes for premium feel
val CocktailsShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// Specialized shapes for different cocktail-themed components
object CocktailShapes {
    val glassCard = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
    val cocktailButton = RoundedCornerShape(20.dp)
    val premiumCard = RoundedCornerShape(16.dp)
    val dialogShape = RoundedCornerShape(24.dp)
}

// Dialog-specific shapes for consistent premium theming
object DialogShapes {
    val default = RoundedCornerShape(24.dp)
    val small = RoundedCornerShape(16.dp)
    val large = RoundedCornerShape(32.dp)
}

val CocktailsTypography = Typography()

@Composable
fun CocktailsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val currentTheme = if(useDarkTheme){
        ThemeMode.DARK
    } else {
        ThemeMode.LIGHT
    }
    
    // Use the new cocktail-themed color schemes
    val colorScheme = if (useDarkTheme) DarkCocktailColors else LightCocktailColors

    // Update status bar when theme changes
    LaunchedEffect(currentTheme) {
        when (currentTheme) {
            ThemeMode.LIGHT -> {
                // Light theme: use dark status bar icons
                updateStatusBarAppearance(isLight = true)
            }
            ThemeMode.DARK -> {
                // Dark theme: use light status bar icons
                updateStatusBarAppearance(isLight = false)
            }
            ThemeMode.SYSTEM -> {
                // System theme: let system decide
                updateStatusBarAppearance(isLight = !useDarkTheme)
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CocktailsTypography,
        shapes = CocktailsShapes
    ) {
        // Apply beautiful gradient background to all screens automatically
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (useDarkTheme) {
                        CocktailGradients.UnifiedDark
                    } else {
                        CocktailGradients.UnifiedLight
                    }
                )
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun DarkCocktailThemePreview() {
    MaterialTheme(
        colorScheme = DarkCocktailColors,
        typography = CocktailsTypography,
        shapes = CocktailsShapes
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = CocktailColors.DeepNavy)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                "Dark Cocktail Theme",
                style = MaterialTheme.typography.headlineSmall,
                color = CocktailColors.GoldenHour,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Primary colors showcase
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    "Primary Container - Luxury Purple",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    "Secondary Container - Deep Surface",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    "Tertiary Container - Card Surface",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun LightCocktailThemePreview() {
    MaterialTheme(
        colorScheme = LightCocktailColors,
        typography = CocktailsTypography,
        shapes = CocktailsShapes
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = CocktailColors.WarmWhite)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                "Light Cocktail Theme",
                style = MaterialTheme.typography.headlineSmall,
                color = CocktailColors.LuxuryPurple,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    "Primary Container - Light Purple",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    "Secondary Container - Warm Orange",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    "Tertiary Container - Mint Green",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
