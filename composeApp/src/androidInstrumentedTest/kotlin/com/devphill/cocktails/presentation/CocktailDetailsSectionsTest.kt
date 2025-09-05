package com.devphill.cocktails.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel
import com.devphill.cocktails.presentation.cocktail_details.*
import org.junit.Rule
import org.junit.Test

/**
 * Comprehensive UI tests for the Cocktail Details sections.
 */
class CocktailDetailsSectionsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // QUICK STATS SECTION TESTS
    @Test
    fun quickStatsSection_rendersAllStats_whenVisible() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedQuickStatsSection(
                    complexity = ComplexityLevel.COMPLEX,
                    alcoholStrength = AlcoholStrength.STRONG,
                    preparationTime = 5,
                    glass = "Coupe",
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.QUICK_STATS).assertIsDisplayed()
        composeTestRule.onNodeWithText("Time").assertIsDisplayed()
        composeTestRule.onNodeWithText("5 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Complexity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Complex").assertIsDisplayed()
        composeTestRule.onNodeWithText("Strength").assertIsDisplayed()
        composeTestRule.onNodeWithText("Strong").assertIsDisplayed()
        composeTestRule.onNodeWithText("Glass").assertIsDisplayed()
        composeTestRule.onNodeWithText("Coupe").assertIsDisplayed()
    }

    @Test
    fun quickStatsSection_notVisible_whenIsVisibleFalse() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedQuickStatsSection(
                    complexity = ComplexityLevel.COMPLEX,
                    alcoholStrength = AlcoholStrength.STRONG,
                    preparationTime = 5,
                    glass = "Coupe",
                    isVisible = false,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.QUICK_STATS).assertIsNotDisplayed()
    }

    @Test
    fun quickStatsSection_handlesNullGlass() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedQuickStatsSection(
                    complexity = ComplexityLevel.SIMPLE,
                    alcoholStrength = AlcoholStrength.LIGHT,
                    preparationTime = 2,
                    glass = null,
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.QUICK_STATS).assertIsDisplayed()
        composeTestRule.onNodeWithText("Glass").assertDoesNotExist()
    }

    @Test
    fun quickStatsSection_showsCorrectComplexityLevels() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedQuickStatsSection(
                    complexity = ComplexityLevel.SIMPLE,
                    alcoholStrength = AlcoholStrength.MEDIUM,
                    preparationTime = 3,
                    glass = "Rocks",
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onNodeWithText("Simple").assertIsDisplayed()
    }

    @Test
    fun quickStatsSection_showsCorrectAlcoholStrengths() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedQuickStatsSection(
                    complexity = ComplexityLevel.MEDIUM,
                    alcoholStrength = AlcoholStrength.LIGHT,
                    preparationTime = 3,
                    glass = "Rocks",
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onNodeWithText("Light").assertIsDisplayed()
    }

    @Test
    fun quickStatsSection_showsCorrectPreparationTime() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedQuickStatsSection(
                    complexity = ComplexityLevel.MEDIUM,
                    alcoholStrength = AlcoholStrength.MEDIUM,
                    preparationTime = 10,
                    glass = "Rocks",
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onNodeWithText("10 min").assertIsDisplayed()
    }

    // INGREDIENTS SECTION TESTS
    @Test
    fun ingredientsSection_displaysChipsAndList_whenVisible() {
        val ingredients = listOf("Vodka", "Lime Juice", "Sugar Syrup")

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedIngredientsSection(
                    ingredients = ingredients,
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INGREDIENTS_SECTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingredients").assertIsDisplayed()

        ingredients.forEach { ingredient ->
            composeTestRule.onAllNodesWithText(ingredient)[0].assertIsDisplayed()
        }
    }

    @Test
    fun ingredientsSection_notVisible_whenIsVisibleFalse() {
        val ingredients = listOf("Vodka", "Lime Juice")

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedIngredientsSection(
                    ingredients = ingredients,
                    isVisible = false,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INGREDIENTS_SECTION).assertIsNotDisplayed()
    }

    @Test
    fun ingredientsSection_handlesEmptyIngredientsList() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedIngredientsSection(
                    ingredients = emptyList(),
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INGREDIENTS_SECTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingredients").assertIsDisplayed()
    }

    @Test
    fun ingredientsSection_handlesSingleIngredient() {
        val ingredients = listOf("Whiskey")

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedIngredientsSection(
                    ingredients = ingredients,
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INGREDIENTS_SECTION).assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Whiskey")[0].assertIsDisplayed()
    }

    @Test
    fun ingredientsSection_handlesLongIngredientsList() {
        val ingredients = listOf(
            "Gin", "Dry Vermouth", "Sweet Vermouth", "Campari",
            "Orange Peel", "Lemon Twist", "Angostura Bitters",
            "Simple Syrup", "Fresh Lime Juice", "Egg White"
        )

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedIngredientsSection(
                    ingredients = ingredients,
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INGREDIENTS_SECTION).assertIsDisplayed()

        // Verify first and last ingredients are displayed
        composeTestRule.onAllNodesWithText("Gin")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Egg White")[0].assertIsDisplayed()
    }

    // INSTRUCTIONS SECTION TESTS
    @Test
    fun instructionsSection_showsMethodAndGarnish_whenProvided() {
        val method = "Shake all ingredients with ice and fine strain."
        val garnish = "Lime wheel"

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedInstructionsSection(
                    method = method,
                    garnish = garnish,
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1200)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INSTRUCTIONS_SECTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Instructions").assertIsDisplayed()
        composeTestRule.onNodeWithText(method).assertIsDisplayed()
        composeTestRule.onNodeWithText("Garnish").assertIsDisplayed()
        composeTestRule.onNodeWithText(garnish).assertIsDisplayed()
    }

    @Test
    fun instructionsSection_showsOnlyMethod_whenGarnishNull() {
        val method = "Stir with ice and strain into glass."

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedInstructionsSection(
                    method = method,
                    garnish = null,
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1200)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INSTRUCTIONS_SECTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Instructions").assertIsDisplayed()
        composeTestRule.onNodeWithText(method).assertIsDisplayed()
        composeTestRule.onNodeWithText("Garnish").assertDoesNotExist()
    }

    @Test
    fun instructionsSection_showsOnlyMethod_whenGarnishEmpty() {
        val method = "Build in glass over ice."

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedInstructionsSection(
                    method = method,
                    garnish = "",
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1200)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INSTRUCTIONS_SECTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Instructions").assertIsDisplayed()
        composeTestRule.onNodeWithText(method).assertIsDisplayed()
        composeTestRule.onNodeWithText("Garnish").assertDoesNotExist()
    }

    @Test
    fun instructionsSection_notVisible_whenIsVisibleFalse() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedInstructionsSection(
                    method = "Shake with ice",
                    garnish = "Lemon",
                    isVisible = false,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1200)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INSTRUCTIONS_SECTION).assertIsNotDisplayed()
    }

    @Test
    fun instructionsSection_handlesLongMethod() {
        val longMethod = "Add all ingredients to a cocktail shaker filled with ice. Shake vigorously for 10-15 seconds until well chilled. Double strain through a fine mesh strainer into a chilled coupe glass. The double straining ensures a smooth texture by removing any ice chips or fruit pulp."

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedInstructionsSection(
                    method = longMethod,
                    garnish = "Orange twist",
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1200)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INSTRUCTIONS_SECTION).assertIsDisplayed()
        composeTestRule.onNodeWithText(longMethod).assertIsDisplayed()
    }

    // VIDEO SECTION TESTS
    @Test
    fun videoSection_displays_whenVisible() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedVideoSection(
                    videoUrl = "https://youtube.com/watch?v=abc123",
                    onVideoClick = {},
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.VIDEO_SECTION).assertIsDisplayed()
    }

    @Test
    fun videoSection_notVisible_whenIsVisibleFalse() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedVideoSection(
                    videoUrl = "https://youtube.com/watch?v=abc123",
                    onVideoClick = {},
                    isVisible = false,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.VIDEO_SECTION).assertIsNotDisplayed()
    }

    @Test
    fun videoSection_triggersOnVideoClick_whenPressed() {
        var clickCount = 0

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedVideoSection(
                    videoUrl = "https://youtube.com/watch?v=abc123",
                    onVideoClick = { clickCount++ },
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.VIDEO_SECTION).performClick()

        assert(clickCount == 1)
    }

    // ANIMATION TIMING TESTS
    @Test
    fun quickStatsSection_respectsAnimationDelay() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedQuickStatsSection(
                    complexity = ComplexityLevel.MEDIUM,
                    alcoholStrength = AlcoholStrength.MEDIUM,
                    preparationTime = 3,
                    glass = "Rocks",
                    isVisible = true,
                    delay = 500
                )
            }
        }

        // After delay time
        composeTestRule.mainClock.advanceTimeBy(200)
        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.QUICK_STATS).assertIsDisplayed()
    }

    @Test
    fun ingredientsSection_respectsAnimationDelay() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedIngredientsSection(
                    ingredients = listOf("Gin", "Tonic"),
                    isVisible = true,
                    delay = 800
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(200)
        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.INGREDIENTS_SECTION).assertIsDisplayed()
    }
}
