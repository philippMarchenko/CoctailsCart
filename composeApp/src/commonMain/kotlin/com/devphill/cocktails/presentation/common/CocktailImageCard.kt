package com.devphill.cocktails.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CocktailImageCard(
    cocktail: Cocktail,
    tags: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            var isLoading by remember { mutableStateOf(true) }

            if (cocktail.imageUrl != null) {
                AsyncImage(
                    model = cocktail.imageUrl,
                    contentDescription = cocktail.title,
                    contentScale = ContentScale.Crop,
                    onLoading = { isLoading = true },
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false },
                    modifier = Modifier.fillMaxSize()
                )

                // Show progress indicator while image is loading
                if (isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp), // Smaller progress indicator
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp // Thinner stroke for better performance
                        )
                    }
                }
            }

            // Dark gradient overlay to improve text contrast
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            // Title at the top
            Text(
                text = cocktail.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            // Tags at the bottom with proper wrapping
            if (tags.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    tags.take(6).forEach { tag ->
                        TagChip(text = tag)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CocktailImageCardPreview() {
    MaterialTheme {
        CocktailImageCard(
            cocktail = Cocktail(
                id = "1",
                title = "Mojito",
                imageUrl = "https://example.com/mojito.jpg",
                cocktailUrl = null,
                category = "Classic",
                categoryEnum = "CLASSIC",
                views = "2.5k",
                ingredients = listOf("White rum", "Lime juice", "Mint", "Sugar", "Soda water"),
                ingredientsEnums = listOf("WHITE_RUM", "LIME_JUICE", "MINT", "SUGAR", "SODA_WATER"),
                method = "Muddle mint and sugar, add rum and lime juice, top with soda water",
                garnish = "Mint sprig",
                glass = "Highball glass",
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.LIGHT,
                searchText = "mojito rum mint lime",
                isFavorite = true,
                preparationTime = 3
            ),
            tags = listOf("Refreshing", "Minty", "Summer"),
            onClick = { }
        )
    }
}



