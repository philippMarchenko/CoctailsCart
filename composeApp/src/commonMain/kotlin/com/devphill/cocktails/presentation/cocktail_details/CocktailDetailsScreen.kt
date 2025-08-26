package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailsScreen(
    cocktail: Cocktail,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onVideoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(cocktail.id) {
        isVisible = false
        delay(100) // Small delay to reset animations
        isVisible = true
    }

    // Use WindowInsets to make content go edge-to-edge
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp) // Remove all padding to go edge-to-edge
    ) {
        item {
            // Full-screen Hero Image Section extending to the very top edge
            AnimatedFullScreenHeroSection(
                imageUrl = cocktail.imageUrl,
                title = cocktail.title,
                category = cocktail.category,
                views = cocktail.views,
                isFavorite = cocktail.isFavorite,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                onShareClick = onShareClick,
                isVisible = isVisible
            )
        }

        item {
            // Quick Stats Section with staggered animation
            AnimatedQuickStatsSection(
                complexity = cocktail.complexity,
                alcoholStrength = cocktail.alcoholStrength,
                preparationTime = cocktail.preparationTime,
                glass = cocktail.glass,
                isVisible = isVisible,
                delay = 300
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Ingredients Section
            AnimatedIngredientsSection(
                ingredients = cocktail.ingredients,
                isVisible = isVisible,
                delay = 600
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Instructions Section
            AnimatedInstructionsSection(
                method = cocktail.method,
                garnish = cocktail.garnish,
                isVisible = isVisible,
                delay = 900
            )
        }

        if (cocktail.videoUrl?.isNotEmpty() == true) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVideoSection(
                    videoUrl = cocktail.videoUrl,
                    onVideoClick = onVideoClick,
                    isVisible = isVisible,
                    delay = 1200
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
        }
    }
}

@Composable
private fun AnimatedFullScreenHeroSection(
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
            .height(500.dp) // Increased height to cover more screen area including status bar space
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

        // Animated Gradient Overlay - stronger at bottom for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f * overlayAlpha), // Lighter at top
                            Color.Black.copy(alpha = 0.4f * overlayAlpha), // Medium in middle
                            Color.Black.copy(alpha = 0.8f * overlayAlpha)  // Stronger at bottom
                        ),
                        startY = 0f,
                        endY = 1500f
                    )
                )
        )

        // Back button positioned at top-left with status bar padding
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
                top = 56.dp, // Extra padding for status bar area
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
                top = 56.dp, // Extra padding for status bar area
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

    // Use absoluteOffset instead of align since we're in a BoxScope context
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

@Composable
private fun AnimatedQuickStatsSection(
    complexity: ComplexityLevel,
    alcoholStrength: AlcoholStrength,
    preparationTime: Int,
    glass: String?,
    isVisible: Boolean,
    delay: Int
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 100,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .graphicsLayer {
                translationY = slideOffset.toFloat()
                this.alpha = alpha
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimatedStatItem(
                icon = Icons.Default.Schedule,
                label = "Time",
                value = "${preparationTime} min",
                isVisible = isVisible,
                delay = delay + 200
            )

            VerticalDivider(
                modifier = Modifier.height(48.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            AnimatedStatItem(
                icon = getComplexityIcon(complexity),
                label = "Complexity",
                value = complexity.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                isVisible = isVisible,
                delay = delay + 300
            )

            VerticalDivider(
                modifier = Modifier.height(48.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            AnimatedStatItem(
                icon = getStrengthIcon(alcoholStrength),
                label = "Strength",
                value = alcoholStrength.name.lowercase().replace("_", " ").split(" ")
                    .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } },
                isVisible = isVisible,
                delay = delay + 400
            )

            glass?.let {
                VerticalDivider(
                    modifier = Modifier.height(48.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                AnimatedStatItem(
                    icon = Icons.Default.LocalBar,
                    label = "Glass",
                    value = it,
                    isVisible = isVisible,
                    delay = delay + 500
                )
            }
        }
    }
}

@Composable
private fun AnimatedStatItem(
    icon: ImageVector,
    label: String,
    value: String,
    isVisible: Boolean,
    delay: Int
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay),
        label = "alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .graphicsLayer { this.alpha = alpha }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AnimatedIngredientsSection(
    ingredients: List<String>,
    isVisible: Boolean,
    delay: Int
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else -80,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .graphicsLayer {
                translationX = slideOffset.toFloat()
                this.alpha = alpha
            }
    ) {
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(ingredients.size) { index ->
                AnimatedIngredientChip(
                    ingredient = ingredients[index],
                    isVisible = isVisible,
                    delay = delay + 200 + (index * 50)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detailed ingredients list with staggered animation
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ingredients.forEachIndexed { index, ingredient ->
                    AnimatedIngredientRow(
                        ingredient = ingredient,
                        isVisible = isVisible,
                        delay = delay + 400 + (index * 100),
                        isLast = index == ingredients.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedIngredientChip(
    ingredient: String,
    isVisible: Boolean,
    delay: Int
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 200, delayMillis = delay),
        label = "alpha"
    )

    AssistChip(
        onClick = { },
        label = {
            Text(
                text = ingredient,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.LocalDining,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        modifier = Modifier
            .scale(scale)
            .graphicsLayer { this.alpha = alpha }
    )
}

@Composable
private fun AnimatedIngredientRow(
    ingredient: String,
    isVisible: Boolean,
    delay: Int,
    isLast: Boolean
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 50,
        animationSpec = tween(durationMillis = 400, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay),
        label = "alpha"
    )

    Column(
        modifier = Modifier.graphicsLayer {
            translationX = slideOffset.toFloat()
            this.alpha = alpha
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                modifier = Modifier.size(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = ingredient,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }

        if (!isLast) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AnimatedInstructionsSection(
    method: String,
    garnish: String?,
    isVisible: Boolean,
    delay: Int
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 80,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .graphicsLayer {
                translationX = slideOffset.toFloat()
                this.alpha = alpha
            }
    ) {
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = method,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                }

                garnish?.takeIf { it.isNotEmpty() }?.let {
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedGarnishSection(
                        garnish = it,
                        isVisible = isVisible,
                        delay = delay + 300
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedGarnishSection(
    garnish: String,
    isVisible: Boolean,
    delay: Int
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 30,
        animationSpec = tween(durationMillis = 400, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay),
        label = "alpha"
    )

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.graphicsLayer {
            translationX = slideOffset.toFloat()
            this.alpha = alpha
        }
    ) {
        Icon(
            imageVector = Icons.Default.LocalFlorist,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "Garnish",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = garnish,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun AnimatedVideoSection(
    videoUrl: String,
    onVideoClick: () -> Unit,
    isVisible: Boolean,
    delay: Int
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 100,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .graphicsLayer {
                translationY = slideOffset.toFloat()
                this.alpha = alpha
            }
    ) {
        Text(
            text = "Tutorial Video",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            onClick = onVideoClick,
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Watch Tutorial",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Text(
                        text = "Learn how to make this cocktail",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

private fun getComplexityIcon(complexity: ComplexityLevel): ImageVector {
    return when (complexity) {
        ComplexityLevel.SIMPLE -> Icons.Default.Speed
        ComplexityLevel.MEDIUM -> Icons.Default.Timeline
        ComplexityLevel.COMPLEX -> Icons.Default.Engineering
    }
}

private fun getStrengthIcon(strength: AlcoholStrength): ImageVector {
    return when (strength) {
        AlcoholStrength.NON_ALCOHOLIC -> Icons.Default.WaterDrop
        AlcoholStrength.LIGHT -> Icons.Default.LocalBar
        AlcoholStrength.MEDIUM -> Icons.Default.LocalBar
        AlcoholStrength.STRONG -> Icons.Default.Sports
    }
}

@Preview
@Composable
private fun CocktailDetailsScreenPreview() {
    MaterialTheme {
        val sampleCocktail = Cocktail(
            id = "1",
            title = "Clover Club",
            imageUrl = "https://iba-world.com/wp-content/uploads/2024/07/iba-cocktail-the-unforgettables-clover-club-66949108a3e54.webp",
            cocktailUrl = "https://iba-world.com/iba-cocktail/clover-club/",
            category = "The unforgettables",
            categoryEnum = "the_unforgettables",
            views = "69.2K views",
            ingredients = listOf("45 ml Gin", "15 ml Raspberry Syrup", "15 ml Fresh Lemon Juice", "Few Drops of Egg White"),
            ingredientsEnums = listOf("gin", "raspberry_syrup", "fresh_lemon_juice", "few_drops_of_egg_white"),
            method = "Pour all ingredients into cocktails shaker, shake well with ice, strain into chilled cocktail glass.",
            garnish = "Garnish with fresh raspberries and lemon twist",
            glass = "Cocktail Glass",
            videoUrl = "https://www.youtube.com/watch?v=oo9R082EgGs",
            complexity = ComplexityLevel.MEDIUM,
            alcoholStrength = AlcoholStrength.LIGHT,
            searchText = "",
            isFavorite = false
        )

        CocktailDetailsScreen(
            cocktail = sampleCocktail,
            onBackClick = { },
            onFavoriteClick = { },
            onShareClick = { },
            onVideoClick = { }
        )
    }
}





