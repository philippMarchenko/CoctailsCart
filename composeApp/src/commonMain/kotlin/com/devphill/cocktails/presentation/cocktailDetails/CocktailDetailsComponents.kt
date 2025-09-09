package com.devphill.cocktails.presentation.cocktailDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cocktailscart.composeapp.generated.resources.Res
import cocktailscart.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import com.devphill.cocktails.domain.model.Cocktail
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailsContent(
    cocktail: Cocktail,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onVideoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Calculate if we should show the fixed toolbar based on scroll position
    val showFixedToolbar by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 300
        }
    }

    LaunchedEffect(cocktail.id) {
        isVisible = false
        delay(100) // Small delay to reset animations
        isVisible = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Main scrollable content
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp),
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
                    isVisible = isVisible,
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
                    delay = 300,
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
                    delay = 600,
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
                    delay = 900,
                )
            }

            if (cocktail.videoUrl?.isNotEmpty() == true) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    AnimatedVideoSection(
                        videoUrl = cocktail.videoUrl,
                        onVideoClick = onVideoClick,
                        isVisible = isVisible,
                        delay = 1200,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
            }
        }

        // Fixed toolbar that appears when scrolling
        AnimatedVisibility(
            visible = showFixedToolbar,
            enter =
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                ) + fadeIn(animationSpec = tween(300)),
            exit =
                slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(200, easing = FastOutLinearInEasing),
                ) + fadeOut(animationSpec = tween(200)),
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(10f),
        ) {
            FixedToolbar(
                title = cocktail.title,
                isFavorite = cocktail.isFavorite,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                onShareClick = onShareClick,
            )
        }
    }
}

@Composable
private fun FixedToolbar(
    title: String,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back),
                    tint = Color.White,
                )
            }

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
            )

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val favoriteScale by animateFloatAsState(
                    targetValue = if (isFavorite) 1.1f else 1f,
                    animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessHigh),
                    label = "favoriteScale",
                )

                IconButton(
                    onClick = onFavoriteClick,
                    modifier =
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                CircleShape,
                            )
                            .scale(favoriteScale),
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) stringResource(Res.string.remove_from_favorites) else stringResource(Res.string.add_to_favorites),
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface,
                    )
                }

                IconButton(
                    onClick = onShareClick,
                    modifier =
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                CircleShape,
                            ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(Res.string.share),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
