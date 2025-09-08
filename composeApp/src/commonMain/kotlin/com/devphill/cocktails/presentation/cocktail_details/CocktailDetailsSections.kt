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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel
import com.devphill.cocktails.presentation.theme.*

object CocktailDetailsTestTags {
    const val QUICK_STATS = "quick_stats_section"
    const val INGREDIENTS_SECTION = "ingredients_section"
    const val INSTRUCTIONS_SECTION = "instructions_section"
    const val GARNISH_SECTION = "garnish_section"
    const val VIDEO_SECTION = "video_section"
}

@Composable
fun AnimatedQuickStatsSection(
    complexity: ComplexityLevel,
    alcoholStrength: AlcoholStrength,
    preparationTime: Int,
    glass: String?,
    isVisible: Boolean,
    delay: Int,
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 100,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha",
    )

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .graphicsLayer {
                    translationY = slideOffset.toFloat()
                    this.alpha = alpha
                }
                .testTag(CocktailDetailsTestTags.QUICK_STATS),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            AnimatedStatItem(
                icon = Icons.Default.Schedule,
                label = "Time",
                value = "$preparationTime min",
                isVisible = isVisible,
                delay = delay + 200,
            )

            VerticalDivider(
                modifier = Modifier.height(48.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            )

            AnimatedStatItem(
                icon = getComplexityIcon(complexity),
                label = "Complexity",
                value = complexity.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                isVisible = isVisible,
                delay = delay + 300,
            )

            VerticalDivider(
                modifier = Modifier.height(48.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            )

            AnimatedStatItem(
                icon = getStrengthIcon(alcoholStrength),
                label = "Strength",
                value =
                    alcoholStrength.name.lowercase().replace("_", " ").split(" ")
                        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } },
                isVisible = isVisible,
                delay = delay + 400,
            )

            glass?.let {
                VerticalDivider(
                    modifier = Modifier.height(48.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                )

                AnimatedStatItem(
                    icon = Icons.Default.LocalBar,
                    label = "Glass",
                    value = it,
                    isVisible = isVisible,
                    delay = delay + 500,
                )
            }
        }
    }
}

@Composable
fun AnimatedIngredientsSection(
    ingredients: List<String>,
    isVisible: Boolean,
    delay: Int,
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else -80,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha",
    )

    Column(
        modifier =
            Modifier
                .padding(horizontal = 16.dp)
                .graphicsLayer {
                    translationX = slideOffset.toFloat()
                    this.alpha = alpha
                }
                .testTag(CocktailDetailsTestTags.INGREDIENTS_SECTION),
    ) {
        CocktailSectionHeader(
            text = "Ingredients",
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
        ) {
            items(ingredients.size) { index ->
                AnimatedIngredientChip(
                    ingredient = ingredients[index],
                    isVisible = isVisible,
                    delay = delay + 200 + (index * 50),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detailed ingredients list with staggered animation
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                ingredients.forEachIndexed { index, ingredient ->
                    AnimatedIngredientRow(
                        ingredient = ingredient,
                        isVisible = isVisible,
                        delay = delay + 400 + (index * 100),
                        isLast = index == ingredients.lastIndex,
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
    delay: Int,
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 80,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha",
    )

    Column(
        modifier =
            Modifier
                .padding(horizontal = 16.dp)
                .graphicsLayer {
                    translationX = slideOffset.toFloat()
                    this.alpha = alpha
                }
                .testTag(CocktailDetailsTestTags.INSTRUCTIONS_SECTION),
    ) {
        CocktailSectionHeader(
            text = "Instructions",
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.Top,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp),
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    CocktailBodyText(
                        text = method,
                        modifier = Modifier.weight(1f),
                    )
                }

                garnish?.takeIf { it.isNotEmpty() }?.let {
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedGarnishSection(
                        garnish = it,
                        isVisible = isVisible,
                        delay = delay + 300,
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
    delay: Int,
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 100,
        animationSpec = tween(durationMillis = 500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay + 100),
        label = "alpha",
    )

    Column(
        modifier =
            Modifier
                .padding(horizontal = 16.dp)
                .graphicsLayer {
                    translationY = slideOffset.toFloat()
                    this.alpha = alpha
                }
                .testTag(CocktailDetailsTestTags.VIDEO_SECTION),
    ) {
        CocktailSectionHeader(
            text = "Tutorial Video",
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            onClick = onVideoClick,
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(32.dp),
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    CocktailCardTitle(
                        text = "Watch Tutorial",
                    )

                    CocktailBodyText(
                        text = "Learn how to make this cocktail",
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}
