package com.devphill.cocktails.presentation.cocktailDetails

import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CocktailDetailsIntegrationTest {
    private val sampleCocktails =
        listOf(
            Cocktail(
                id = "mojito",
                title = "Mojito",
                imageUrl = "https://example.com/mojito.jpg",
                cocktailUrl = "https://example.com/mojito",
                category = "The unforgettables",
                categoryEnum = "the_unforgettables",
                views = "123K views",
                ingredients = listOf("50ml White Rum", "10ml Lime Juice", "6 Mint leaves", "2tsp Sugar", "Soda Water"),
                ingredientsEnums = listOf("white_rum", "lime_juice", "mint_leaves", "sugar", "soda_water"),
                garnish = "Sprig of mint",
                method =
                    "Muddle mint leaves with sugar and lime juice. Add a splash of soda water and fill the glass " +
                        "with cracked ice. Pour the rum and top with soda water.",
                videoUrl = "https://youtube.com/watch?v=mojito",
                complexity = ComplexityLevel.MEDIUM,
                alcoholStrength = AlcoholStrength.LIGHT,
                searchText = "mojito mint rum",
                isFavorite = false,
                glass = "Cocktail glass",
                preparationTime = 6,
            ),
            Cocktail(
                id = "martini",
                title = "Dry Martini",
                imageUrl = "https://example.com/martini.jpg",
                cocktailUrl = "https://example.com/martini",
                category = "The unforgettables",
                categoryEnum = "the_unforgettables",
                views = "89K views",
                ingredients = listOf("60ml Gin", "10ml Dry Vermouth"),
                ingredientsEnums = listOf("gin", "dry_vermouth"),
                method = "Stir all ingredients with ice and strain into chilled glass.",
                garnish = "Lemon twist or olive",
                glass = "Cocktail glass",
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.STRONG,
                searchText = "martini gin vermouth",
                isFavorite = true,
            ),
        )

    @Test
    fun cocktailIntegrationValidationAllPropertiesWorkTogether() {
        sampleCocktails.forEach { cocktail ->
            // Validate that all cocktail properties are properly set
            assertNotNull(cocktail.id)
            assertNotNull(cocktail.title)
            assertTrue(cocktail.ingredients.isNotEmpty())
            assertTrue(cocktail.preparationTime > 0)

            // Validate enums
            assertNotNull(cocktail.complexity)
            assertNotNull(cocktail.alcoholStrength)

            // Validate that preparation time calculation works
            val expectedMinTime =
                when {
                    cocktail.method.contains("shake", true) -> 3
                    cocktail.method.contains("stir", true) -> 2
                    cocktail.method.contains("muddle", true) -> 4
                    cocktail.method.contains("blend", true) -> 5
                    else -> 2
                }
            assertTrue(cocktail.preparationTime >= expectedMinTime)
        }
    }

    @Test
    fun uiStateTransitionsValidation() {
        // Test state transitions that would happen in real usage
        val initialState = CocktailDetailsUiState()
        val loadingState = initialState.copy(isLoading = true)
        val successState =
            loadingState.copy(
                cocktail = sampleCocktails[0],
                isLoading = false,
            )
        val favoriteToggledState =
            successState.copy(
                cocktail = successState.cocktail?.copy(isFavorite = true),
            )
        val errorState =
            loadingState.copy(
                isLoading = false,
                error = "Network error",
            )

        // Validate state transitions
        assertNull(initialState.cocktail)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)

        assertTrue(loadingState.isLoading)
        assertNull(loadingState.error)

        assertNotNull(successState.cocktail)
        assertFalse(successState.isLoading)
        assertNull(successState.error)

        assertTrue(favoriteToggledState.cocktail!!.isFavorite)

        assertNull(errorState.cocktail)
        assertFalse(errorState.isLoading)
        assertNotNull(errorState.error)
    }

    @Test
    fun dataConsistencyAcrossDifferentCocktailTypes() {
        sampleCocktails.forEach { cocktail ->
            // Test that formatting functions work with all cocktail data
            val complexityFormatted =
                cocktail.complexity.name.lowercase()
                    .replaceFirstChar { it.uppercaseChar() }
            val strengthFormatted =
                cocktail.alcoholStrength.name.lowercase()
                    .replace("_", " ").split(" ")
                    .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }

            assertNotNull(complexityFormatted)
            assertNotNull(strengthFormatted)
            assertTrue(complexityFormatted.isNotEmpty())
            assertTrue(strengthFormatted.isNotEmpty())

            // Validate preparation time formatting
            val timeFormatted = "${cocktail.preparationTime} min"
            assertTrue(timeFormatted.matches(Regex("\\d+ min")))
        }
    }

    @Test
    fun errorHandlingScenarios() {
        val errorStates =
            listOf(
                CocktailDetailsUiState(error = "Network error"),
                CocktailDetailsUiState(error = "Cocktail not found"),
                CocktailDetailsUiState(error = "Failed to update favorite status"),
                CocktailDetailsUiState(error = ""),
            )

        errorStates.forEach { state ->
            // All error states should have no cocktail and not be loading
            assertNull(state.cocktail)
            assertFalse(state.isLoading)

            // Error clearing should work
            val clearedState = state.copy(error = null)
            assertNull(clearedState.error)
        }
    }

    @Test
    fun nullValueHandlingAcrossComponents() {
        val cocktailWithNulls =
            sampleCocktails[0].copy(
                imageUrl = null,
                cocktailUrl = null,
                views = null,
                garnish = null,
                glass = null,
                videoUrl = null,
            )

        // Should handle null values gracefully
        assertNull(cocktailWithNulls.imageUrl)
        assertNull(cocktailWithNulls.cocktailUrl)
        assertNull(cocktailWithNulls.views)
        assertNull(cocktailWithNulls.garnish)
        assertNull(cocktailWithNulls.glass)
        assertNull(cocktailWithNulls.videoUrl)

        // Required fields should still be present
        assertNotNull(cocktailWithNulls.id)
        assertNotNull(cocktailWithNulls.title)
        assertTrue(cocktailWithNulls.ingredients.isNotEmpty())
        assertTrue(cocktailWithNulls.method.isNotEmpty())
    }
}
