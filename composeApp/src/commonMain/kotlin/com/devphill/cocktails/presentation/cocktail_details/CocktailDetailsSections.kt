package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel

@Composable
fun AnimatedQuickStatsSection(
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
fun AnimatedIngredientsSection(
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
fun AnimatedInstructionsSection(
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
                    verticalAlignment = androidx.compose.ui.Alignment.Top
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
fun AnimatedVideoSection(
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
                modifier = Modifier.padding(16.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
