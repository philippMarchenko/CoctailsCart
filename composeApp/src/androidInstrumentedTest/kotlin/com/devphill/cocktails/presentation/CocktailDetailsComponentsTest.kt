package com.devphill.cocktails.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
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
import com.devphill.cocktails.presentation.cocktailDetails.CocktailDetailsContent
import org.junit.Rule
import org.junit.Test

/**
 * Comprehensive UI tests for the main Cocktail Details Components.
 */
class CocktailDetailsComponentsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createSampleCocktail(
        id: String = "1",
        title: String = "Margarita",
        category: String = "Classic Cocktails",
        views: String? = "1.2K views",
        ingredients: List<String> = listOf("Tequila", "Lime Juice", "Triple Sec"),
        method: String = "Shake with ice and strain",
        garnish: String? = "Lime wheel",
        glass: String? = "Coupe",
        videoUrl: String? = "https://youtube.com/watch?v=abc123",
        complexity: ComplexityLevel = ComplexityLevel.MEDIUM,
        alcoholStrength: AlcoholStrength = AlcoholStrength.MEDIUM,
        preparationTime: Int = 5,
        isFavorite: Boolean = false,
    ) = Cocktail(
        id = id,
        title = title,
        imageUrl = "https://example.com/image.jpg",
        cocktailUrl = "https://example.com/cocktail",
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
    fun cocktailDetailsContent_displaysAllSections_whenCocktailProvided() {
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        // Allow animations to complete
        composeTestRule.mainClock.advanceTimeBy(2000)

        // Verify hero section elements
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.2K views").assertIsDisplayed()

        // Verify quick stats section
        composeTestRule.onNodeWithTag("quick_stats_section").assertIsDisplayed()
        composeTestRule.onNodeWithText("5 min").assertIsDisplayed()

        // Verify complexity and strength separately to avoid ambiguity
        val quickStatsNode = composeTestRule.onNodeWithTag("quick_stats_section")
        quickStatsNode.onChildren().filter(hasText("Medium"))
            .assertCountEquals(2) // Both complexity and strength are Medium

        composeTestRule.onNodeWithText("Coupe").assertIsDisplayed()

        // Verify ingredients section
        composeTestRule.onNodeWithTag("ingredients_section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingredients").assertIsDisplayed()
        cocktail.ingredients.forEach { ingredient ->
            composeTestRule.onAllNodesWithText(ingredient)[0].assertIsDisplayed()
        }

        // Verify instructions section
        composeTestRule.onNodeWithTag("instructions_section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Instructions").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shake with ice and strain").assertIsDisplayed()
        composeTestRule.onNodeWithText("Garnish").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lime wheel").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsContent_handlesMinimalCocktail() {
        val cocktail =
            createSampleCocktail(
                views = null,
                garnish = null,
                glass = null,
                videoUrl = null,
                ingredients = emptyList(),
            )

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Title and category should still be displayed
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()

        // Quick stats should still appear but without glass
        composeTestRule.onNodeWithTag("quick_stats_section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Glass").assertDoesNotExist()

        // Instructions should appear without garnish
        composeTestRule.onNodeWithTag("instructions_section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Garnish").assertDoesNotExist()

        // Video section should not appear
        composeTestRule.onNodeWithTag("video_section").assertDoesNotExist()
    }

    @Test
    fun cocktailDetailsContent_triggersBackClick() {
        var backClickCount = 0
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = { backClickCount++ },
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        assert(backClickCount == 1)
    }

    @Test
    fun cocktailDetailsContent_triggersFavoriteClick() {
        var favoriteClickCount = 0
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = { favoriteClickCount++ },
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Add to favorites").performClick()

        assert(favoriteClickCount == 1)
    }

    @Test
    fun cocktailDetailsContent_triggersShareClick() {
        var shareClickCount = 0
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = { shareClickCount++ },
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Share").performClick()

        assert(shareClickCount == 1)
    }

    @Test
    fun cocktailDetailsContent_handlesComplexCocktail() {
        val cocktail =
            createSampleCocktail(
                title = "Complex Negroni Variation with Multiple Ingredients",
                category = "Contemporary Classics and Modern Interpretations",
                views = "15.7M views",
                ingredients =
                    listOf(
                        "45ml Premium Gin",
                        "30ml Sweet Vermouth",
                        "30ml Campari",
                        "15ml Aperol",
                        "2 dashes Orange Bitters",
                        "1 dash Angostura Bitters",
                        "Orange peel for garnish",
                        "Luxardo cherry for garnish",
                    ),
                method =
                    "Add all spirits and bitters to a mixing glass filled with ice. Stir gently for" +
                        " 30 seconds to achieve proper dilution. Strain into a chilled old fashioned glass over one " +
                        "large ice cube. Express the oils from the orange peel over the drink and drop into the " +
                        "glass. Garnish with a Luxardo cherry on a cocktail pick.",
                garnish = "Orange peel and Luxardo cherry",
                glass = "Old Fashioned Glass",
                complexity = ComplexityLevel.COMPLEX,
                alcoholStrength = AlcoholStrength.STRONG,
                preparationTime = 8,
            )

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Verify complex content is displayed
        composeTestRule.onNodeWithText("Complex Negroni Variation with Multiple Ingredients")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Contemporary Classics and Modern Interpretations")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("15.7M views").assertIsDisplayed()

        // Verify complex stats
        composeTestRule.onNodeWithText("8 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Complex").assertIsDisplayed()
        composeTestRule.onNodeWithText("Strong").assertIsDisplayed()
        composeTestRule.onNodeWithText("Old Fashioned Glass").assertIsDisplayed()

        // Verify some ingredients are displayed
        composeTestRule.onAllNodesWithText("45ml Premium Gin")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("30ml Sweet Vermouth")[0].assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsContent_handlesSimpleCocktail() {
        val cocktail =
            createSampleCocktail(
                title = "Gin & Tonic",
                category = "Simple",
                views = "500 views",
                ingredients = listOf("Gin", "Tonic Water"),
                method = "Build in glass over ice.",
                garnish = "Lime wedge",
                glass = "Highball",
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.LIGHT,
                preparationTime = 1,
            )

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Verify simple content is displayed
        composeTestRule.onNodeWithText("Gin & Tonic").assertIsDisplayed()
        composeTestRule.onNodeWithText("500 views").assertIsDisplayed()

        // Verify there are exactly 2 "Simple" text nodes (category and complexity)
        composeTestRule.onAllNodesWithText("Simple").assertCountEquals(2)

        // Verify simple stats
        composeTestRule.onNodeWithText("1 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Light").assertIsDisplayed()
        composeTestRule.onNodeWithText("Highball").assertIsDisplayed()

        // Verify simple ingredients
        composeTestRule.onAllNodesWithText("Gin")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Tonic Water")[0].assertIsDisplayed()

        // Verify simple method
        composeTestRule.onNodeWithText("Build in glass over ice.").assertIsDisplayed()

        // Verify simple garnish
        composeTestRule.onNodeWithText("Lime wedge").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsContent_showsFavoriteState_whenIsFavoriteTrue() {
        val cocktail = createSampleCocktail(isFavorite = true)

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Remove from favorites")
            .assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsContent_scrollableBehavior() {
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Test that content is scrollable
        composeTestRule.onRoot().performTouchInput {
            swipeUp()
        }

        // Content should still be accessible after scrolling
        composeTestRule.onNodeWithText("Instructions").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsContent_handlesEmptyIngredients() {
        val cocktail = createSampleCocktail(ingredients = emptyList())

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000)

        // Ingredients section should still be displayed
        composeTestRule.onNodeWithTag("ingredients_section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingredients").assertIsDisplayed()
    }

    @Test
    fun cocktailDetailsContent_animationSequence() {
        val cocktail = createSampleCocktail()

        composeTestRule.setContent {
            MaterialTheme {
                CocktailDetailsContent(
                    cocktail = cocktail,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onVideoClick = {},
                )
            }
        }

        // Test animation sequence timing

        // Hero should appear first (immediate)
        composeTestRule.mainClock.advanceTimeBy(100)
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()

        // Quick stats should appear after delay (300ms)
        composeTestRule.mainClock.advanceTimeBy(400)
        composeTestRule.onNodeWithTag("quick_stats_section").assertIsDisplayed()

        // Ingredients should appear after delay (600ms total)
        composeTestRule.mainClock.advanceTimeBy(400)
        composeTestRule.onNodeWithTag("ingredients_section").assertIsDisplayed()

        // Instructions and video should appear after delay (900ms total)
        composeTestRule.mainClock.advanceTimeBy(400)
        composeTestRule.onNodeWithTag("instructions_section").assertIsDisplayed()
    }
}
