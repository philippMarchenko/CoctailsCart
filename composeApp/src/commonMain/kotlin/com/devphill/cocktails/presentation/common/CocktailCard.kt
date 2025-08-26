package com.devphill.cocktails.presentation.common
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CocktailCard(
    cocktail: Cocktail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = cocktail.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = cocktail.method,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ComplexityChip(complexity = cocktail.complexity)

                    cocktail.views?.let { views ->
                        Text(
                            text = "👁️ $views",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (cocktail.alcoholStrength != com.devphill.cocktails.domain.model.AlcoholStrength.NON_ALCOHOLIC) {
                        Text(
                            text = "🍺 ${cocktail.alcoholStrength.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (cocktail.isFavorite) {
                        Text(
                            text = "❤️",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CocktailCardPreview() {
    MaterialTheme {
        CocktailCard(
            cocktail = Cocktail(
                id = "1",
                title = "Margarita",
                imageUrl = null,
                cocktailUrl = null,
                category = "Classic",
                categoryEnum = "CLASSIC",
                views = "1.2k",
                ingredients = listOf("Tequila", "Lime juice", "Triple sec"),
                ingredientsEnums = listOf("TEQUILA", "LIME_JUICE", "TRIPLE_SEC"),
                method = "Shake with ice and strain into a salt-rimmed glass",
                garnish = "Lime wheel",
                glass = "Margarita glass",
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = com.devphill.cocktails.domain.model.AlcoholStrength.MEDIUM,
                searchText = "margarita tequila lime",
                isFavorite = true,
                preparationTime = 5
            ),
            onClick = { }
        )
    }
}
