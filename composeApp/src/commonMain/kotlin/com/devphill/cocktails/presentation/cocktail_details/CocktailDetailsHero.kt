package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun AnimatedFullScreenHeroSection(
    imageUrl: String?,
    title: String,
    category: String,
    views: String?,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    isVisible: Boolean
) {
    val imageScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 1.1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "imageScale"
    )

    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) 0.7f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 100),
        label = "overlayAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        // Full-screen Background Image with scale animation
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .fillMaxSize()
                .scale(imageScale),
            contentScale = ContentScale.Crop
        )

        // Animated Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f * overlayAlpha),
                            Color.Black.copy(alpha = 0.4f * overlayAlpha),
                            Color.Black.copy(alpha = 0.8f * overlayAlpha)
                        ),
                        startY = 0f,
                        endY = 1500f
                    )
                )
        )

        // Back button positioned at top-left
        AnimatedBackButton(
            isVisible = isVisible,
            onBackClick = onBackClick
        )

        // Action buttons (favorite & share) positioned at top-right
        AnimatedActionButtons(
            isVisible = isVisible,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick,
            onShareClick = onShareClick
        )

        // Title, category, and views positioned at bottom of image
        Box(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            AnimatedBottomContent(
                category = category,
                title = title,
                views = views,
                isVisible = isVisible
            )
        }
    }
}

@Composable
private fun AnimatedBackButton(
    isVisible: Boolean,
    onBackClick: () -> Unit
) {
    val backButtonOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else -100,
        animationSpec = tween(durationMillis = 500, delayMillis = 100, easing = FastOutSlowInEasing),
        label = "backButtonOffset"
    )

    val backButtonAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 150),
        label = "backButtonAlpha"
    )

    Box(
        modifier = Modifier
            .padding(
                top = 56.dp,
                start = 16.dp
            )
            .graphicsLayer {
                translationY = backButtonOffset.toFloat()
                alpha = backButtonAlpha
            }
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.6f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AnimatedActionButtons(
    isVisible: Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val buttonsOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else -100,
        animationSpec = tween(durationMillis = 500, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "buttonsOffset"
    )

    val buttonsAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 250),
        label = "buttonsAlpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 56.dp,
                end = 16.dp
            )
            .graphicsLayer {
                translationY = buttonsOffset.toFloat()
                alpha = buttonsAlpha
            },
        horizontalArrangement = Arrangement.End
    ) {
        val favoriteScale by animateFloatAsState(
            targetValue = if (isFavorite) 1.2f else 1f,
            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessHigh),
            label = "favoriteScale"
        )

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.6f),
                    CircleShape
                )
                .scale(favoriteScale)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (isFavorite) Color.Red else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.6f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AnimatedBottomContent(
    category: String,
    title: String,
    views: String?,
    isVisible: Boolean
) {
    val contentOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 150,
        animationSpec = tween(durationMillis = 600, delayMillis = 300, easing = FastOutSlowInEasing),
        label = "contentOffset"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 400),
        label = "contentAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .graphicsLayer {
                translationY = contentOffset.toFloat()
                alpha = contentAlpha
            }
    ) {
        // Category badge
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Cocktail title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
        )

        // Views count
        views?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
