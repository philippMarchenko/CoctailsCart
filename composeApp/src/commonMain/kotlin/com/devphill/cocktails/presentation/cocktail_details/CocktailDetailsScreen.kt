package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import com.devphill.cocktails.presentation.theme.CocktailsTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CocktailDetailsScreen(
    cocktail: Cocktail,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onVideoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CocktailDetailsContent(
        cocktail = cocktail,
        onBackClick = onBackClick,
        onFavoriteClick = onFavoriteClick,
        onShareClick = onShareClick,
        onVideoClick = onVideoClick,
        modifier = modifier
    )
}

// Preview
@Preview
@Composable
private fun CocktailDetailsScreenPreview() {
    CocktailsTheme {
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
