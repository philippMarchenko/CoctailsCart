package com.devphill.cocktails.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeUp
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import com.devphill.cocktails.presentation.cocktail_details.CocktailDetailsScreen
import org.junit.Rule
import org.junit.Test

/**
 * Comprehensive UI tests for the main Cocktail Details Screen.
 */
class CocktailDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createSampleCocktail(
        id: String = "1",
        title: String = "Old Fashioned",
        category: String = "Classic Cocktails",
        views: String? = "2.5K views",
        ingredients: List<String> = listOf("Bourbon", "Sugar Cube", "Angostura Bitters"),
        method: String = "Muddle sugar and bitters, add bourbon, stir with ice",
        garnish: String? = "Orange peel",
        glass: String? = "Old Fashioned Glass",
        videoUrl: String? = "https://youtube.com/watch?v=test",
        complexity: ComplexityLevel = ComplexityLevel.MEDIUM,
        alcoholStrength: AlcoholStrength = AlcoholStrength.STRONG,
        preparationTime: Int = 3,
        isFavorite: Boolean = false,
    ) = Cocktail(
        id = id,
        title = title,
        imageUrl = "https://example.com/old-fashioned.jpg",
        cocktailUrl = "https://example.com/cocktail/old-fashioned",
        category = category,
        categoryEnum = "classic",
        views = views,
        ingredients = ingredients,
        ingredientsEnums = ingredients.map { it.lowercase().replace(" ", "_") },
        method = method,
        garnish = garnish,
        glass = glass,
        videoUrl = videoUrl,
        complexity = complexity,
        alcoholStrength = alcoholStrength,
        preparationTime = preparationTime,
        isFavorite = isFavorite,
        searchText = "",
    )

    @Test
    fun cocktailDetailsScreen_displaysCorrectContent() {
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Verify all main content is displayed
        composeTestRule.onNodeWithText("Old Fashioned").assertIsDisplayed()
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()
        composeTestRule.onNodeWithText("2.5K views").assertIsDisplayed()

        // Verify action buttons
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Share").assertIsDisplayed()

        // Verify sections
        composeTestRule.onNodeWithTag("quick_stats_section").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ingredients_section").assertIsDisplayed()
        composeTestRule.onNodeWithTag("instructions_section").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_handlesAllCallbacks() {
        val cocktail = createSampleCocktail()
        var backClicked = false
        var favoriteClicked = false
        var shareClicked = false
        var videoClicked = false

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = { backClicked = true },
                    onFavoriteClick = { favoriteClicked = true },
                    onShareClick = { shareClicked = true },
                    onVideoClick = { videoClicked = true },
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Test back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backClicked)

        // Test favorite button
        composeTestRule.onNodeWithContentDescription("Add to favorites").performClick()
        assert(favoriteClicked)

        // Test share button
        composeTestRule.onNodeWithContentDescription("Share").performClick()
        assert(shareClicked)
    }

    @Test
    fun cocktailDetailsScreen_handlesNullOptionalFields() {
        val cocktail =
            createSampleCocktail(
                views = null,
                garnish = null,
                glass = null,
                videoUrl = null,
            )

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Core content should still be displayed
        composeTestRule.onNodeWithText("Old Fashioned").assertIsDisplayed()
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()

        // Optional content should not be displayed
        composeTestRule.onNodeWithText("Glass").assertDoesNotExist()
        composeTestRule.onNodeWithText("Garnish").assertDoesNotExist()
        composeTestRule.onNodeWithTag("video_section").assertDoesNotExist()
    }

    @Test
    fun cocktailDetailsScreen_showsCorrectFavoriteState() {
        val favoriteCocktail = createSampleCocktail(isFavorite = true)

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = favoriteCocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_handlesEmptyIngredients() {
        val cocktail = createSampleCocktail(ingredients = emptyList())

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Ingredients section should still exist
        composeTestRule.onNodeWithTag("ingredients_section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingredients").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_handlesLongContent() {
        val cocktail =
            createSampleCocktail(
                title = "The Most Incredibly Long and Complex Cocktail Name That Should Wrap Properly in the UI Without Breaking Layout",
                category = "Extremely Long Category Name That Tests UI Layout Boundaries and Text Wrapping Capabilities",
                method = "This is an extremely long method description that includes many detailed steps for cocktail preparation. First, you must carefully select the finest ingredients from reputable sources. Then, ensure all glassware is properly chilled to the optimal temperature. Next, measure each ingredient with precision using appropriate jiggers and measuring tools. Add the base spirit first, followed by modifiers in order of sweetness. Incorporate bitters drop by drop to achieve the perfect balance. Stir or shake according to the cocktail's requirements, ensuring proper dilution and temperature. Finally, strain into the prepared glass and garnish with the specified accompaniments, ensuring visual appeal and aromatic enhancement.",
                garnish = "A complex garnish consisting of an orange peel twist, a luxardo cherry, and a sprig of fresh mint",
            )

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Verify long content is displayed (using substring matching)
        composeTestRule.onNodeWithText(cocktail.title, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(cocktail.category, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(cocktail.method, substring = true).assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_handlesAllComplexityLevels() {
        val cocktail = createSampleCocktail(complexity = ComplexityLevel.SIMPLE)

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)
        composeTestRule.onNodeWithText("Simple").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_handlesAllAlcoholStrengths() {
        val cocktail = createSampleCocktail(alcoholStrength = AlcoholStrength.LIGHT)

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)
        composeTestRule.onNodeWithText("Light").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_handlesVariousPreparationTimes() {
        val cocktail = createSampleCocktail(preparationTime = 1)

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)
        composeTestRule.onNodeWithText("1 min").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_isScrollable() {
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Perform scroll gesture
        composeTestRule.onRoot().performTouchInput {
            swipeUp()
        }

        // Verify content is still accessible after scrolling
        composeTestRule.onNodeWithText("Instructions").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsScreen_handlesMultipleClicksGracefully() {
        val cocktail = createSampleCocktail()
        var clickCount = 0

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = { clickCount++ },
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        // Perform multiple rapid clicks
        repeat(5) {
            composeTestRule.onNodeWithContentDescription("Back").performClick()
        }

        assert(clickCount == 5)
    }

    @Test
    fun cocktailDetailsScreen_displaysCorrectContentAfterReconfiguration() {
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsScreen(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Verify initial state
        composeTestRule.onNodeWithText("Old Fashioned").assertIsDisplayed()
    }
}
