package com.devphill.cocktails.presentation.cocktailDetails

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.devphill.cocktails.presentation.theme.CocktailBodyText
import com.devphill.cocktails.presentation.theme.CocktailLabel

@Composable
internal fun AnimatedStatItem(
    icon: ImageVector,
    label: String,
    value: String,
    isVisible: Boolean,
    delay: Int,
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(durationMillis = 400, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "scale",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay),
        label = "alpha",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .scale(scale)
                .graphicsLayer { this.alpha = alpha },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        CocktailLabel(
            text = label,
        )

        CocktailBodyText(
            text = value,
        )
    }
}

@Composable
internal fun AnimatedIngredientChip(
    ingredient: String,
    isVisible: Boolean,
    delay: Int,
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "scale",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 200, delayMillis = delay),
        label = "alpha",
    )

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier =
            Modifier
                .scale(scale)
                .graphicsLayer { this.alpha = alpha },
    ) {
        CocktailLabel(
            text = ingredient,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@Composable
internal fun AnimatedIngredientRow(
    ingredient: String,
    isVisible: Boolean,
    delay: Int,
    isLast: Boolean,
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 50,
        animationSpec = tween(durationMillis = 400, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay),
        label = "alpha",
    )

    Column(
        modifier =
            Modifier.graphicsLayer {
                translationX = slideOffset.toFloat()
                this.alpha = alpha
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(6.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            CocktailBodyText(
                text = ingredient,
                modifier = Modifier.weight(1f),
            )
        }

        if (!isLast) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
internal fun AnimatedGarnishSection(
    garnish: String,
    isVisible: Boolean,
    delay: Int,
) {
    val slideOffset by animateIntAsState(
        targetValue = if (isVisible) 0 else 30,
        animationSpec = tween(durationMillis = 400, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "slideOffset",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = delay),
        label = "alpha",
    )

    Row(
        modifier =
            Modifier.graphicsLayer {
                translationX = slideOffset.toFloat()
                this.alpha = alpha
            },
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Default.LocalFlorist,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            CocktailLabel(
                text = "Garnish",
            )

            CocktailBodyText(
                text = garnish,
            )
        }
    }
}
