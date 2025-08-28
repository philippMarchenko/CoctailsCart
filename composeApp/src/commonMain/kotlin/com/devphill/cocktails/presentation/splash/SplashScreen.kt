package com.devphill.cocktails.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.devphill.cocktails.data.preferences.UserPreferencesManager
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(
    userPreferencesManager: UserPreferencesManager,
    onNavigateToSignIn: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    // Multiple animated values for sophisticated animations
    val logoAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, delayMillis = 300),
        label = "logoAlpha"
    )

    val logoScale = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val titleSlideUp = animateFloatAsState(
        targetValue = if (showContent) 0f else 100f,
        animationSpec = tween(durationMillis = 800, delayMillis = 800),
        label = "titleSlide"
    )

    val titleAlpha = animateFloatAsState(
        targetValue = if (showContent) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 1000),
        label = "titleAlpha"
    )

    val taglineAlpha = animateFloatAsState(
        targetValue = if (showContent) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 1200),
        label = "taglineAlpha"
    )

    // Rotating animation for decorative elements
    val infiniteRotation = rememberInfiniteTransition(label = "rotation")
    val rotationAngle by infiniteRotation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    // Floating animation for bubbles
    val floatingOffset by infiniteRotation.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(500)
        showContent = true
        delay(2500)

        // Check authentication status
        val isLoggedIn = userPreferencesManager.isUserLoggedIn()
        if (isLoggedIn) {
            onNavigateToMain()
        } else {
            onNavigateToSignIn()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F0C29)
                    ),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background decorative elements
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawCocktailBackground(
                rotationAngle = rotationAngle,
                alpha = if (startAnimation) 0.1f else 0f
            )
        }

        // Floating bubbles
        FloatingBubbles(
            modifier = Modifier.fillMaxSize(),
            offset = floatingOffset,
            alpha = if (startAnimation) 0.6f else 0f
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Main logo card with cocktail glass design
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .alpha(logoAlpha.value)
                    .scale(logoScale.value),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D1B69).copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(80.dp)) {
                        drawCocktailGlass()
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // App name with gradient text effect
            Text(
                text = "CocktailsCraft",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.displayMedium.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFD700),
                            Color(0xFFFFA500),
                            Color(0xFFFF6B6B)
                        )
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(y = titleSlideUp.value.dp)
                    .alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stylish tagline
            Text(
                text = "Craft • Discover • Enjoy",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFB8B8D1),
                textAlign = TextAlign.Center,
                letterSpacing = 2.sp,
                modifier = Modifier.alpha(taglineAlpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Premium Cocktail Experience",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF9A9AB0),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }

        // Decorative elements in corners
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (startAnimation) 0.3f else 0f)
        ) {
            // Top left decoration
            Canvas(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopStart)
                    .offset((-20).dp, (-20).dp)
                    .rotate(rotationAngle * 0.5f)
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF6B6B).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    radius = 50.dp.toPx()
                )
            }

            // Bottom right decoration
            Canvas(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .offset(20.dp, 20.dp)
                    .rotate(-rotationAngle * 0.3f)
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    radius = 60.dp.toPx()
                )
            }
        }
    }
}

@Composable
private fun FloatingBubbles(
    modifier: Modifier = Modifier,
    offset: Float,
    alpha: Float
) {
    Canvas(modifier = modifier.alpha(alpha)) {
        val bubbles = listOf(
            Triple(size.width * 0.2f, size.height * 0.3f, 8f),
            Triple(size.width * 0.8f, size.height * 0.2f, 12f),
            Triple(size.width * 0.15f, size.height * 0.7f, 6f),
            Triple(size.width * 0.85f, size.height * 0.8f, 10f),
            Triple(size.width * 0.6f, size.height * 0.15f, 4f),
            Triple(size.width * 0.3f, size.height * 0.9f, 7f)
        )

        bubbles.forEach { (x, y, radius) ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                ),
                radius = radius,
                center = Offset(x, y + offset * (radius / 10f))
            )
        }
    }
}

private fun DrawScope.drawCocktailGlass() {
    val glassColor = Color(0xFFFFD700)
    val liquidColor = Color(0xFFFF6B6B)

    // Draw martini glass bowl
    val glassPath = Path().apply {
        moveTo(size.width * 0.2f, size.height * 0.3f)
        lineTo(size.width * 0.8f, size.height * 0.3f)
        lineTo(size.width * 0.7f, size.height * 0.5f)
        lineTo(size.width * 0.3f, size.height * 0.5f)
        close()
    }

    // Draw stem
    drawRect(
        color = glassColor,
        topLeft = Offset(size.width * 0.48f, size.height * 0.5f),
        size = androidx.compose.ui.geometry.Size(size.width * 0.04f, size.height * 0.3f)
    )

    // Draw base
    drawRect(
        color = glassColor,
        topLeft = Offset(size.width * 0.3f, size.height * 0.8f),
        size = androidx.compose.ui.geometry.Size(size.width * 0.4f, size.height * 0.05f)
    )

    // Draw glass outline
    drawPath(
        path = glassPath,
        color = glassColor,
        style = Stroke(width = 3.dp.toPx())
    )

    // Draw liquid
    val liquidPath = Path().apply {
        moveTo(size.width * 0.25f, size.height * 0.35f)
        lineTo(size.width * 0.75f, size.height * 0.35f)
        lineTo(size.width * 0.68f, size.height * 0.45f)
        lineTo(size.width * 0.32f, size.height * 0.45f)
        close()
    }

    drawPath(
        path = liquidPath,
        brush = Brush.linearGradient(
            colors = listOf(liquidColor.copy(alpha = 0.8f), liquidColor.copy(alpha = 0.4f))
        )
    )

    // Olive garnish
    drawCircle(
        color = Color(0xFF90EE90),
        radius = 4.dp.toPx(),
        center = Offset(size.width * 0.6f, size.height * 0.25f)
    )
}

private fun DrawScope.drawCocktailBackground(rotationAngle: Float, alpha: Float) {
    rotate(rotationAngle) {
        // Draw decorative cocktail-themed shapes
        val colors = listOf(
            Color(0xFFFF6B6B).copy(alpha = alpha),
            Color(0xFFFFD700).copy(alpha = alpha),
            Color(0xFF4ECDC4).copy(alpha = alpha)
        )

        repeat(8) { index ->
            val angle = (index * 45f) * (kotlin.math.PI / 180f)
            val radius = size.minDimension * 0.4f
            val x = size.center.x + cos(angle) * radius
            val y = size.center.y + sin(angle) * radius

            drawCircle(
                color = colors[index % colors.size],
                radius = 20f,
                center = Offset(x.toFloat(), y.toFloat())
            )
        }
    }
}