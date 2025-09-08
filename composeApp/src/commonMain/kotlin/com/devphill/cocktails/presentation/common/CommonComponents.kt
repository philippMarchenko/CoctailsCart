package com.devphill.cocktails.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devphill.cocktails.domain.model.ComplexityLevel
import com.devphill.cocktails.presentation.theme.*

@Composable
fun TagChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
    ) {
        CocktailLabel(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
fun CategoryChip(
    category: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        onClick = onClick,
        label = {
            CocktailLabel(text = category)
        },
        selected = false,
        modifier = modifier,
    )
}

@Composable
fun ComplexityChip(
    complexity: ComplexityLevel,
    modifier: Modifier = Modifier,
) {
    val (text, color) =
        when (complexity) {
            ComplexityLevel.SIMPLE -> "Simple" to MaterialTheme.colorScheme.secondary
            ComplexityLevel.MEDIUM -> "Medium" to MaterialTheme.colorScheme.tertiary
            ComplexityLevel.COMPLEX -> "Complex" to MaterialTheme.colorScheme.error
        }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
    ) {
        CocktailLabel(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
fun CocktailIngredientsList(
    ingredients: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CocktailSectionHeader(
            text = "Ingredients",
            modifier = Modifier.padding(bottom = 8.dp),
        )

        ingredients.forEach { ingredient ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CocktailBodyText(
                    text = "• ",
                    modifier = Modifier,
                )
                CocktailBodyText(
                    text = ingredient,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        CocktailScreenTitle(
            text = "⚠️",
            modifier = Modifier.padding(bottom = 8.dp),
        )

        ErrorText(
            text = message,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        onRetry?.let { retry ->
            Button(
                onClick = retry,
                modifier = Modifier.padding(top = 16.dp),
            ) {
                CocktailBodyText("Retry")
            }
        }
    }
}
