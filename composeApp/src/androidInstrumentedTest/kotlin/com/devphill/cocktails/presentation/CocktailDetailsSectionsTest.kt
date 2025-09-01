package com.devphill.cocktails.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel
import com.devphill.cocktails.presentation.cocktail_details.AnimatedIngredientsSection
import com.devphill.cocktails.presentation.cocktail_details.AnimatedInstructionsSection
import com.devphill.cocktails.presentation.cocktail_details.AnimatedQuickStatsSection
import com.devphill.cocktails.presentation.cocktail_details.AnimatedVideoSection
import com.devphill.cocktails.presentation.cocktail_details.CocktailDetailsTestTags
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the Cocktail Details sections.
 */
class CocktailDetailsSectionsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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

        // Verify ingredients are present without requiring unique instances
        ingredients.forEach { ingredient ->
            // Check that at least one node with this text exists
            composeTestRule.onAllNodesWithText(ingredient)[0].assertIsDisplayed()
        }
    }

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
    fun videoSection_displaysTitleAndDescription() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedVideoSection(
                    videoUrl = "https://example.com/video.mp4",
                    onVideoClick = {},
                    isVisible = true,
                    delay = 0
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(800)

        composeTestRule.onNodeWithTag(CocktailDetailsTestTags.VIDEO_SECTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Tutorial Video").assertIsDisplayed()
        composeTestRule.onNodeWithText("Watch Tutorial").assertIsDisplayed()
        composeTestRule.onNodeWithText("Learn how to make this cocktail").assertIsDisplayed()
    }
}

