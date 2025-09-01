package com.devphill.cocktails.domain.interactor

import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow

/**
 * Interface for cocktail-related business logic operations.
 *
 * This interface acts as an intermediary between ViewModels and the Repository,
 * providing a clean interface for all cocktail operations while encapsulating
 * business logic and data transformations.
 *
 * ## Architecture Flow
 * ViewModel → CocktailInteractor → CocktailRepository → DataSources
 */
interface CocktailInteractor {
    /**
     * Retrieves all cocktails as a reactive data stream.
     *
     * @return Flow of all cocktails from the repository
     */
    suspend fun getAllCocktails(): Flow<List<Cocktail>>

    /**
     * Searches for cocktails matching the given query.
     *
     * @param query The search term to match against cocktail data
     * @return Flow of cocktails matching the search query
     */
    suspend fun searchCocktails(query: String): Flow<List<Cocktail>>

    /**
     * Retrieves all favorite cocktails.
     *
     * @return Flow of cocktails marked as favorites
     */
    suspend fun getFavoriteCocktails(): Flow<List<Cocktail>>

    /**
     * Retrieves a specific cocktail by its unique identifier.
     *
     * @param id The unique identifier of the cocktail
     * @return The cocktail if found, null otherwise
     */
    suspend fun getCocktailById(id: String): Cocktail?

    /**
     * Retrieves cocktails filtered by category.
     *
     * @param category The category to filter by
     * @return Flow of cocktails in the specified category
     */
    suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>>

    /**
     * Toggles the favorite status of a cocktail.
     *
     * This method handles the business logic of determining whether to add or remove
     * a cocktail from favorites based on its current state.
     *
     * @param cocktail The cocktail to toggle favorite status for
     * @param isFavorite Current favorite status (true if currently favorite)
     */
    suspend fun toggleFavorite(cocktail: Cocktail, isFavorite: Boolean)
}