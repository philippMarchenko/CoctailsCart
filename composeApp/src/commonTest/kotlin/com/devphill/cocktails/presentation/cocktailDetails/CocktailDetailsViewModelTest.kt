package com.devphill.cocktails.presentation.cocktailDetails

import app.cash.turbine.test
import com.devphill.cocktails.domain.interactor.CocktailInteractor
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Manual mock implementation of CocktailInteractor for cross-platform testing
 * This replaces MockK to ensure compatibility with both Android and iOS targets
 */
class FakeCocktailInteractor : CocktailInteractor {
    // Mock state for getCocktailById
    var getCocktailByIdResult: Cocktail? = null
    var getCocktailByIdException: Exception? = null
    var getCocktailByIdCallCount = 0
    var lastCocktailIdRequested: String? = null

    // Mock state for toggleFavorite
    var toggleFavoriteException: Exception? = null
    var toggleFavoriteCallCount = 0
    var lastToggleFavoriteCall: Pair<Cocktail, Boolean>? = null

    override suspend fun getAllCocktails(): Flow<List<Cocktail>> = flowOf(emptyList())

    override suspend fun searchCocktails(query: String): Flow<List<Cocktail>> = flowOf(emptyList())

    override suspend fun getFavoriteCocktails(): Flow<List<Cocktail>> = flowOf(emptyList())

    override suspend fun getCocktailById(id: String): Cocktail? {
        getCocktailByIdCallCount++
        lastCocktailIdRequested = id

        getCocktailByIdException?.let { throw it }
        return getCocktailByIdResult
    }

    override suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> = flowOf(emptyList())

    override suspend fun toggleFavorite(
        cocktail: Cocktail,
        isFavorite: Boolean,
    ) {
        toggleFavoriteCallCount++
        lastToggleFavoriteCall = Pair(cocktail, isFavorite)

        toggleFavoriteException?.let { throw it }
    }

    fun reset() {
        getCocktailByIdResult = null
        getCocktailByIdException = null
        getCocktailByIdCallCount = 0
        lastCocktailIdRequested = null
        toggleFavoriteException = null
        toggleFavoriteCallCount = 0
        lastToggleFavoriteCall = null
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CocktailDetailsViewModelTest {
    private lateinit var viewModel: CocktailDetailsViewModel
    private lateinit var fakeCocktailInteractor: FakeCocktailInteractor
    private val testDispatcher = StandardTestDispatcher()

    private val sampleCocktail =
        Cocktail(
            id = "1",
            title = "Mojito",
            imageUrl = "https://example.com/mojito.jpg",
            cocktailUrl = "https://example.com/mojito",
            category = "Refreshing",
            categoryEnum = "REFRESHING",
            views = "1000",
            ingredients = listOf("White Rum", "Sugar", "Lime Juice", "Soda Water", "Mint"),
            ingredientsEnums = listOf("WHITE_RUM", "SUGAR", "LIME_JUICE", "SODA_WATER", "MINT"),
            method = "Muddle mint leaves with sugar and lime juice. Add rum and top with soda water.",
            garnish = "Fresh mint sprig",
            glass = "Highball glass",
            videoUrl = "https://example.com/mojito-video",
            complexity = ComplexityLevel.SIMPLE,
            alcoholStrength = AlcoholStrength.MEDIUM,
            searchText = "mojito rum mint lime refreshing",
            isFavorite = false,
            preparationTime = 5,
        )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeCocktailInteractor = FakeCocktailInteractor()
        viewModel = CocktailDetailsViewModel(fakeCocktailInteractor)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        fakeCocktailInteractor.reset()
    }

    @Test
    fun initialStateShouldBeCorrect() =
        runTest {
            viewModel.uiState.test {
                val initialState = awaitItem()
                assertNull(initialState.cocktail)
                assertFalse(initialState.isLoading)
                assertNull(initialState.error)
            }
        }

    @Test
    fun loadCocktailShouldShowLoadingStateAndThenSuccessStateWhenCocktailIsFound() =
        runTest {
            // Given
            fakeCocktailInteractor.getCocktailByIdResult = sampleCocktail

            // When
            viewModel.uiState.test {
                val initialState = awaitItem()
                assertNull(initialState.cocktail)
                assertFalse(initialState.isLoading)

                viewModel.loadCocktail("1")

                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)
                assertNull(loadingState.error)

                val successState = awaitItem()
                assertFalse(successState.isLoading)
                assertNull(successState.error)
                assertEquals(sampleCocktail, successState.cocktail)
            }

            // Then
            assertEquals(1, fakeCocktailInteractor.getCocktailByIdCallCount)
            assertEquals("1", fakeCocktailInteractor.lastCocktailIdRequested)
        }

    @Test
    fun loadCocktailShouldNotShowLoadingWhenReloadingTheSameCocktail() =
        runTest {
            // Given
            fakeCocktailInteractor.getCocktailByIdResult = sampleCocktail

            viewModel.uiState.test {
                awaitItem() // initial state

                // Load cocktail first time
                viewModel.loadCocktail("1")
                awaitItem() // loading state
                val firstLoadState = awaitItem() // success state
                assertEquals(sampleCocktail, firstLoadState.cocktail)

                // Reset call count to track the second call
                val initialCallCount = fakeCocktailInteractor.getCocktailByIdCallCount

                // Load same cocktail again
                viewModel.loadCocktail("1")

                // Should still make the call but not show loading
                // We need to advance the test scheduler to process the coroutine
                testScheduler.advanceUntilIdle()

                // Verify the call was made
                assertEquals(initialCallCount + 1, fakeCocktailInteractor.getCocktailByIdCallCount)

                // The current state should still have the cocktail and not be loading
                val currentState = viewModel.uiState.value
                assertFalse(currentState.isLoading)
                assertEquals(sampleCocktail, currentState.cocktail)
                assertNull(currentState.error)
            }
        }

    @Test
    fun loadCocktailShouldShowLoadingWhenLoadingADifferentCocktail() =
        runTest {
            // Given
            val cocktail2 = sampleCocktail.copy(id = "2", title = "Margarita")
            fakeCocktailInteractor.getCocktailByIdResult = sampleCocktail

            viewModel.uiState.test {
                awaitItem() // initial state

                // Load first cocktail
                viewModel.loadCocktail("1")
                awaitItem() // loading state
                awaitItem() // success state

                // Change the mock result for the second cocktail
                fakeCocktailInteractor.getCocktailByIdResult = cocktail2

                // Load different cocktail
                viewModel.loadCocktail("2")
                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)

                val successState = awaitItem()
                assertFalse(successState.isLoading)
                assertEquals(cocktail2, successState.cocktail)
            }
        }

    @Test
    fun loadCocktailShouldSetErrorWhenCocktailIsNotFound() =
        runTest {
            // Given
            fakeCocktailInteractor.getCocktailByIdResult = null

            // When
            viewModel.uiState.test {
                awaitItem() // initial state

                viewModel.loadCocktail("999")
                awaitItem() // loading state

                val errorState = awaitItem()
                assertFalse(errorState.isLoading)
                assertNull(errorState.cocktail)
                assertEquals("Cocktail not found", errorState.error)
            }

            // Then
            assertEquals("999", fakeCocktailInteractor.lastCocktailIdRequested)
        }

    @Test
    fun loadCocktailShouldSetErrorWhenExceptionOccurs() =
        runTest {
            // Given
            val errorMessage = "Network error"
            fakeCocktailInteractor.getCocktailByIdException = RuntimeException(errorMessage)

            // When
            viewModel.uiState.test {
                awaitItem() // initial state

                viewModel.loadCocktail("1")
                awaitItem() // loading state

                val errorState = awaitItem()
                assertFalse(errorState.isLoading)
                assertNull(errorState.cocktail)
                assertEquals(errorMessage, errorState.error)
            }
        }

    @Test
    fun loadCocktailShouldSetUnknownErrorWhenExceptionHasNoMessage() =
        runTest {
            // Given
            fakeCocktailInteractor.getCocktailByIdException = RuntimeException()

            // When
            viewModel.uiState.test {
                awaitItem() // initial state

                viewModel.loadCocktail("1")
                awaitItem() // loading state

                val errorState = awaitItem()
                assertFalse(errorState.isLoading)
                assertNull(errorState.cocktail)
                assertEquals("Unknown error occurred", errorState.error)
            }
        }

    @Test
    fun toggleFavoriteShouldDoNothingWhenNoCocktailIsLoaded() =
        runTest {
            // When
            viewModel.uiState.test {
                awaitItem()

                viewModel.toggleFavorite()

                // Should not emit any new state
                expectNoEvents()
            }

            // Then
            assertEquals(0, fakeCocktailInteractor.toggleFavoriteCallCount)
        }

    @Test
    fun toggleFavoriteShouldAddToFavoritesWhenCocktailIsNotFavorite() =
        runTest {
            // Given
            val nonFavoriteCocktail = sampleCocktail.copy(isFavorite = false)
            fakeCocktailInteractor.getCocktailByIdResult = nonFavoriteCocktail

            viewModel.uiState.test {
                awaitItem() // initial state

                // Load cocktail first
                viewModel.loadCocktail("1")
                awaitItem() // loading state
                val loadedState = awaitItem() // success state
                assertFalse(loadedState.cocktail!!.isFavorite)

                // Toggle favorite
                viewModel.toggleFavorite()

                val favoriteState = awaitItem()
                assertTrue(favoriteState.cocktail!!.isFavorite)
                assertNull(favoriteState.error)
            }

            // Then
            assertEquals(1, fakeCocktailInteractor.toggleFavoriteCallCount)
            assertEquals(Pair(nonFavoriteCocktail, false), fakeCocktailInteractor.lastToggleFavoriteCall)
        }

    @Test
    fun toggleFavoriteShouldRemoveFromFavoritesWhenCocktailIsFavorite() =
        runTest {
            // Given
            val favoriteCocktail = sampleCocktail.copy(isFavorite = true)
            fakeCocktailInteractor.getCocktailByIdResult = favoriteCocktail

            viewModel.uiState.test {
                awaitItem() // initial state

                // Load cocktail first
                viewModel.loadCocktail("1")
                awaitItem() // loading state
                val loadedState = awaitItem() // success state
                assertTrue(loadedState.cocktail!!.isFavorite)

                // Toggle favorite
                viewModel.toggleFavorite()

                val nonFavoriteState = awaitItem()
                assertFalse(nonFavoriteState.cocktail!!.isFavorite)
                assertNull(nonFavoriteState.error)
            }

            // Then
            assertEquals(1, fakeCocktailInteractor.toggleFavoriteCallCount)
            assertEquals(Pair(favoriteCocktail, true), fakeCocktailInteractor.lastToggleFavoriteCall)
        }

    @Test
    fun toggleFavoriteShouldSetErrorWhenExceptionOccurs() =
        runTest {
            // Given
            val favoriteCocktail = sampleCocktail.copy(isFavorite = false)
            fakeCocktailInteractor.getCocktailByIdResult = favoriteCocktail
            fakeCocktailInteractor.toggleFavoriteException = RuntimeException("Database error")

            viewModel.uiState.test {
                awaitItem() // initial state

                // Load cocktail first
                viewModel.loadCocktail("1")
                awaitItem() // loading state
                awaitItem() // success state

                // Toggle favorite
                viewModel.toggleFavorite()

                val errorState = awaitItem()
                // Cocktail state should remain unchanged
                assertEquals(favoriteCocktail, errorState.cocktail)
                assertEquals("Failed to update favorite status", errorState.error)
            }
        }

    @Test
    fun clearErrorShouldRemoveErrorFromState() =
        runTest {
            // Given - first create an error state
            fakeCocktailInteractor.getCocktailByIdResult = null

            viewModel.uiState.test {
                awaitItem() // initial state

                // Create error state
                viewModel.loadCocktail("999")
                awaitItem() // loading state
                val errorState = awaitItem() // error state
                assertNotNull(errorState.error)

                // Clear error
                viewModel.clearError()

                val clearedState = awaitItem()
                assertNull(clearedState.error)
                // Other state should remain the same
                assertEquals(errorState.cocktail, clearedState.cocktail)
                assertEquals(errorState.isLoading, clearedState.isLoading)
            }
        }

    @Test
    fun clearErrorShouldNotEmitWhenThereIsNoError() =
        runTest {
            // When
            viewModel.uiState.test {
                val initialState = awaitItem()
                assertNull(initialState.error)

                viewModel.clearError()

                // Should not emit any new state since there's no error to clear
                expectNoEvents()
            }
        }

    @Test
    fun multipleLoadCocktailCallsShouldHandleConcurrentRequestsCorrectly() =
        runTest {
            // Given
            val cocktail1 = sampleCocktail.copy(id = "1", title = "Mojito")
            val cocktail2 = sampleCocktail.copy(id = "2", title = "Margarita")

            fakeCocktailInteractor.getCocktailByIdResult = cocktail2 // Last call wins

            viewModel.uiState.test {
                awaitItem() // initial state

                // Start multiple concurrent loads
                viewModel.loadCocktail("1")
                viewModel.loadCocktail("2")

                // Should see loading state
                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)

                // Final state should be from the last request
                val finalState = awaitItem()
                assertFalse(finalState.isLoading)
                assertEquals("2", finalState.cocktail?.id)
                assertEquals("Margarita", finalState.cocktail?.title)
            }
        }

    @Test
    fun stateShouldMaintainCocktailDataWhenClearingError() =
        runTest {
            // Given
            fakeCocktailInteractor.getCocktailByIdResult = sampleCocktail
            fakeCocktailInteractor.toggleFavoriteException = RuntimeException("Error")

            viewModel.uiState.test {
                awaitItem() // initial state

                // Load cocktail
                viewModel.loadCocktail("1")
                awaitItem() // loading state
                awaitItem() // success state

                // Create error through toggleFavorite
                viewModel.toggleFavorite()
                val errorState = awaitItem()
                assertNotNull(errorState.error)
                assertEquals(sampleCocktail, errorState.cocktail)

                // Clear error
                viewModel.clearError()
                val clearedState = awaitItem()

                assertNull(clearedState.error)
                assertEquals(sampleCocktail, clearedState.cocktail)
                assertFalse(clearedState.isLoading)
            }
        }
}
