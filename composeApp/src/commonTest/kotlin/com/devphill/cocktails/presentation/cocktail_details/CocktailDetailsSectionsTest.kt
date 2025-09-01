package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CocktailDetailsSectionsTest {

    @Test
    fun quickStatsSection_rendersAllStats_whenVisible() = runComposeUiTest {
        setContent {
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
        mainClock.advanceTimeBy(1000)
        onNodeWithTag(CocktailDetailsTestTags.QUICK_STATS).assertIsDisplayed()
        onNodeWithText("Time").assertIsDisplayed()
        onNodeWithText("5 min").assertIsDisplayed()
        onNodeWithText("Complexity").assertIsDisplayed()
        onNodeWithText("Complex").assertIsDisplayed()
        onNodeWithText("Strength").assertIsDisplayed()
        onNodeWithText("Strong").assertIsDisplayed()
        onNodeWithText("Glass").assertIsDisplayed()
        onNodeWithText("Coupe").assertIsDisplayed()
    }

    @Test
    fun ingredientsSection_displaysChipsAndList_whenVisible() = runComposeUiTest {
        val ingredients = listOf("Vodka", "Lime Juice", "Sugar Syrup")
        setContent {
            MaterialTheme {
                AnimatedIngredientsSection(
                    ingredients = ingredients,
                    isVisible = true,
                    delay = 0
                )
            }
        }
        mainClock.advanceTimeBy(1500)
        onNodeWithTag(CocktailDetailsTestTags.INGREDIENTS_SECTION).assertIsDisplayed()
        onNodeWithText("Ingredients").assertIsDisplayed()
        ingredients.forEach { onNodeWithText(it).assertIsDisplayed() }
    }

    @Test
    fun instructionsSection_showsMethodAndGarnish_whenProvided() = runComposeUiTest {
        val method = "Shake all ingredients with ice and fine strain."
        val garnish = "Lime wheel"
        setContent {
            MaterialTheme {
                AnimatedInstructionsSection(
                    method = method,
                    garnish = garnish,
                    isVisible = true,
                    delay = 0
                )
            }
        }
        mainClock.advanceTimeBy(1200)
        onNodeWithTag(CocktailDetailsTestTags.INSTRUCTIONS_SECTION).assertIsDisplayed()
        onNodeWithText("Instructions").assertIsDisplayed()
        onNodeWithText(method).assertIsDisplayed()
        onNodeWithText("Garnish").assertIsDisplayed()
        onNodeWithText(garnish).assertIsDisplayed()
    }

    @Test
    fun videoSection_displaysTitleAndDescription() = runComposeUiTest {
        setContent {
            MaterialTheme {
                AnimatedVideoSection(
                    videoUrl = "https://example.com/video.mp4",
                    onVideoClick = {},
                    isVisible = true,
                    delay = 0
                )
            }
        }
        mainClock.advanceTimeBy(800)
        onNodeWithTag(CocktailDetailsTestTags.VIDEO_SECTION).assertIsDisplayed()
        onNodeWithText("Tutorial Video").assertIsDisplayed()
        onNodeWithText("Watch Tutorial").assertIsDisplayed()
        onNodeWithText("Learn how to make this cocktail").assertIsDisplayed()
    }
}
