package com.devphill.cocktails.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Typography

private val DarkColors: ColorScheme = darkColorScheme(
    primary = Color(0xFFFFB86C),
    onPrimary = Color(0xFF2B1700),
    primaryContainer = Color(0xFF3C2A1C),
    onPrimaryContainer = Color(0xFFFFDDB8),

    secondary = Color(0xFF9AD18B),
    onSecondary = Color(0xFF0E1B10),
    secondaryContainer = Color(0xFF243424),
    onSecondaryContainer = Color(0xFFBEE7B4),

    tertiary = Color(0xFF80D8E2),
    onTertiary = Color(0xFF001F23),
    tertiaryContainer = Color(0xFF1B3A3F),
    onTertiaryContainer = Color(0xFFBEEAF0),

    background = Color(0xFF0F1115),
    onBackground = Color(0xFFE6E6E6),
    surface = Color(0xFF15181E),
    onSurface = Color(0xFFE6E6E6),
    surfaceVariant = Color(0xFF20242B),
    onSurfaceVariant = Color(0xFFB6BCC6),
    outline = Color(0xFF3B3F46),
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

val CocktailsTypography = Typography()

@Composable
fun CocktailsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CocktailsTypography,
        shapes = CocktailsShapes,
        content = content
    )
}

