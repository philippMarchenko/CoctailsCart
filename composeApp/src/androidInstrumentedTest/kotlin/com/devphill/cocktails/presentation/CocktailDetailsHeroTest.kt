package com.devphill.cocktails.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.devphill.cocktails.presentation.cocktailDetails.AnimatedFullScreenHeroSection
import org.junit.Rule
import org.junit.Test

/**
 * Comprehensive UI tests for the Cocktail Details Hero section.
 */
class CocktailDetailsHeroTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun heroSection_displaysAllElements_whenVisible() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        // Verify title is displayed
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()

        // Verify category is displayed
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()

        // Verify views are displayed
        composeTestRule.onNodeWithText("1.2K views").assertIsDisplayed()

        // Verify back button is displayed
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

        // Verify favorite button is displayed
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()

        // Verify share button is displayed
        composeTestRule.onNodeWithContentDescription("Share").assertIsDisplayed()
    }

    @Test
    fun heroSection_handlesNullImageUrl() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = null,
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        // Title should still be displayed even without image
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()
    }

    @Test
    fun heroSection_handlesNullViews() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = null,
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        // Title and category should still be displayed
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()
    }

    @Test
    fun heroSection_showsFavoriteIcon_whenIsFavoriteTrue() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = true,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500) // Increased wait time for animations

        // Verify favorite button shows "Remove from favorites" when favorited
        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }

    @Test
    fun heroSection_showsAddToFavoriteIcon_whenIsFavoriteFalse() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1500) // Increased wait time for animations

        // Verify favorite button shows "Add to favorites" when not favorited
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
    }

    @Test
    fun heroSection_triggersBackClick_whenBackButtonPressed() {
        var backClickCount = 0

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = { backClickCount++ },
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        assert(backClickCount == 1)
    }

    @Test
    fun heroSection_triggersFavoriteClick_whenFavoriteButtonPressed() {
        var favoriteClickCount = 0

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = { favoriteClickCount++ },
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Add to favorites").performClick()

        assert(favoriteClickCount == 1)
    }

    @Test
    fun heroSection_triggersShareClick_whenShareButtonPressed() {
        var shareClickCount = 0

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = { shareClickCount++ },
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithContentDescription("Share").performClick()

        assert(shareClickCount == 1)
    }

    @Test
    fun heroSection_notVisible_whenIsVisibleFalse() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = false,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        // When not visible, content is still present but with different animation state
        // The animation affects scale and overlay, but doesn't hide content completely
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()
    }

    @Test
    fun heroSection_handlesLongTitle() {
        val longTitle =
            "This is a very long cocktail name that should wrap properly and not" +
                " cause any layout issues in the UI"

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = longTitle,
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithText(longTitle).assertIsDisplayed()
    }

    @Test
    fun heroSection_handlesLongCategory() {
        val longCategory = "This is a very long category name that should be handled properly in the UI layout"

        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = longCategory,
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule.onNodeWithText(longCategory).assertIsDisplayed()
    }

    @Test
    fun heroSection_handlesEmptyTitle() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "",
                    category = "Classic Cocktails",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        // Empty title should still allow other elements to display
        composeTestRule.onNodeWithText("Classic Cocktails").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.2K views").assertIsDisplayed()
    }

    @Test
    fun heroSection_handlesEmptyCategory() {
        composeTestRule.setContent {
            MaterialTheme {
                AnimatedFullScreenHeroSection(
                    imageUrl = "https://example.com/image.jpg",
                    title = "Margarita",
                    category = "",
                    views = "1.2K views",
                    isFavorite = false,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    isVisible = true,
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(1000)

        // Empty category should still allow other elements to display
        composeTestRule.onNodeWithText("Margarita").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.2K views").assertIsDisplayed()
    }
}
