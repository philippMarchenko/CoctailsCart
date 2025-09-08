package com.devphill.cocktails.domain.datasource

import com.devphill.cocktails.domain.model.Cocktail
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface for local cocktail storage operations.
 * This abstraction allows the domain layer to be independent of specific storage implementations.
 */
interface DatabaseCocktailDataSource {
    /**
     * Retrieves all cocktails from the data source.
     * @return Flow emitting list of all cocktails
     */
    fun getAllCocktails(): Flow<List<Cocktail>>

    /**
     * Retrieves a specific cocktail by its unique identifier.
     * @param id The unique cocktail identifier
     * @return Cocktail if found, null otherwise
     */
    suspend fun getCocktailById(id: String): Cocktail?

    /**
     * Retrieves cocktails filtered by category.
     * @param category The category to filter by
     * @return Flow emitting list of cocktails in the specified category
     */
    fun getCocktailsByCategory(category: String): Flow<List<Cocktail>>

    /**
     * Retrieves user's favorite cocktails.
     * @return Flow emitting list of favorite cocktails
     */
    fun getFavoriteCocktails(): Flow<List<Cocktail>>

    /**
     * Searches cocktails by query string.
     * @param query Search term to match against cocktail data
     * @return Flow emitting list of matching cocktails
     */
    fun searchCocktails(query: String): Flow<List<Cocktail>>

    /**
     * Inserts multiple cocktails into the data source.
     * @param cocktails List of cocktails to insert
     */
    suspend fun insertCocktails(cocktails: List<Cocktail>)

    /**
     * Updates the favorite status of a specific cocktail.
     * @param cocktailId The ID of the cocktail to update
     * @param isFavorite New favorite status
     */
    suspend fun updateFavoriteStatus(
        cocktailId: String,
        isFavorite: Boolean,
    )

    /**
     * Gets the total count of cocktails in the data source.
     * @return Number of cocktails stored
     */
    suspend fun getCocktailsCount(): Int

    /**
     * Checks if the data source is empty.
     * @return true if no cocktails are stored, false otherwise
     */
    suspend fun isEmpty(): Boolean
}
