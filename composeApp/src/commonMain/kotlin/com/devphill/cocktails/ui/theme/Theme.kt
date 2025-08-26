package com.devphill.cocktails.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
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

private val DarkColors: ColorScheme = darkColorScheme(
    primary = Color(0xFFFCCA38F),
    onPrimary = Color(0xFF2B1700),
    primaryContainer = Color(0xFF332117),
    onPrimaryContainer = Color(0xFFFFDDB8),

    secondary = Color(0xFF9AD18B),
    onSecondary = Color(0xFF0E1B10),
    secondaryContainer = Color(0xFF243424),
    onSecondaryContainer = Color(0xFFBEE7B4),

    tertiary = Color(0xFF80D8E2),
    onTertiary = Color(0xFF001F23),
    tertiaryContainer = Color(0xFF1B3A3F),
    onTertiaryContainer = Color(0xFFBEEAF0),

    background = Color(0xFF24170F),
    onBackground = Color(0xFFE6E6E6),
    surface = Color(0xFF15181E),
    onSurface = Color(0xFFE6E6E6),
    surfaceVariant = Color(0xFF20242B),
    onSurfaceVariant = Color(0xFFB6BCC6),
    outline = Color(0xFF3B3F46),
    
    // Light status bar for better visibility
    surfaceBright = Color(0xFF2A2D35),
)

private val LightColors: ColorScheme = lightColorScheme(
    primary = Color(0xFF8A4FFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),

    secondary = Color(0xFF4CAF50),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1E7D3),
    onSecondaryContainer = Color(0xFF0E1B10),

    tertiary = Color(0xFF00BCD4),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB2EBF2),
    onTertiaryContainer = Color(0xFF001F23),

    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1B1B1F),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1B1B1F),
    surfaceVariant = Color(0xFFE6E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF7A757F),
)

val CocktailsShapes = Shapes(
    extraSmall = RoundedCornerShape(8),
    small = RoundedCornerShape(12),
    medium = RoundedCornerShape(16),
    large = RoundedCornerShape(24),
    extraLarge = RoundedCornerShape(28)
)

// Dialog-specific shapes for consistent theming
object DialogShapes {
    val default = RoundedCornerShape(18.dp)
    val small = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(24.dp)
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
    
    val colorScheme = if (useDarkTheme) DarkColors else LightColors

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
        shapes = CocktailsShapes,
        content = content
    )
}

@Preview
@Composable
fun LightColorPalettePreview() {
    MaterialTheme(
        colorScheme = LightColors,
        typography = CocktailsTypography,
        shapes = CocktailsShapes
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Color.White),
        ) {
            Text(
                "Light Theme",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ColorSchemeDisplay(LightColors)
        }
    }
}

@Preview
@Composable
fun DarkColorPalettePreview() {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = CocktailsTypography,
        shapes = CocktailsShapes
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Color(0xFF0F1115)), // Use actual dark background
        ) {
            Text(
                "Dark Theme",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFFE6E6E6) // Light text for dark background
            )
            ColorSchemeDisplay(DarkColors)
        }
    }
}

@Preview
@Composable
fun ColorPalettePreview() {
    // Neutral background for comparison
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Color(0xFFF5F5F5)), // Light gray background
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Light Theme Column
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Light Theme",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Black
            )
            ColorSchemeDisplay(LightColors)
        }

        // Dark Theme Column
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Dark Theme",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Black
            )
            ColorSchemeDisplay(DarkColors)
        }
    }
}

@Composable
private fun ColorSchemeDisplay(colorScheme: ColorScheme) {
    Column {
        ColorItem("Primary", colorScheme.primary, colorScheme.onPrimary)
        ColorItem("Primary Container", colorScheme.primaryContainer, colorScheme.onPrimaryContainer)
        ColorItem("Secondary", colorScheme.secondary, colorScheme.onSecondary)
        ColorItem("Secondary Container", colorScheme.secondaryContainer, colorScheme.onSecondaryContainer)
        ColorItem("Tertiary", colorScheme.tertiary, colorScheme.onTertiary)
        ColorItem("Tertiary Container", colorScheme.tertiaryContainer, colorScheme.onTertiaryContainer)
        ColorItem("Background", colorScheme.background, colorScheme.onBackground)
        ColorItem("Surface", colorScheme.surface, colorScheme.onSurface)
        ColorItem("Surface Variant", colorScheme.surfaceVariant, colorScheme.onSurfaceVariant)
        ColorItem("Outline", colorScheme.outline, colorScheme.outline)
    }
}

@Composable
private fun ColorItem(name: String, backgroundColor: Color, textColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Text(
                text = name,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "#${(backgroundColor.value shr 8).toString(16).uppercase().substring(0, 8)}",
                color = textColor
            )
        }
    }
}
