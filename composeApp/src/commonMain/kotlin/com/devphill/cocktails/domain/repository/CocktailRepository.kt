package com.devphill.cocktails.domain.repository

import com.devphill.cocktails.domain.model.Cocktail
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing cocktail data operations.
 * Provides access to cocktail information including favorites and search functionality.
 */
interface CocktailRepository {
    /**
     * Retrieves all available cocktails as a reactive stream.
     * @return Flow emitting list of all cocktails
     */
    suspend fun getAllCocktails(): Flow<List<Cocktail>>

    /**
     * Finds a specific cocktail by its unique identifier.
     * @param id The unique cocktail identifier
     * @return Cocktail if found, null otherwise
     */
    suspend fun getCocktailById(id: String): Cocktail?

    /**
     * Searches cocktails by name or ingredients matching the query.
     * @param query Search term to match against cocktail data
     * @return Flow emitting list of matching cocktails
     */
    suspend fun searchCocktails(query: String): Flow<List<Cocktail>>

    /**
     * Retrieves user's favorite cocktails as a reactive stream.
     * @return Flow emitting list of favorite cocktails
     */
    suspend fun getFavoriteCocktails(): Flow<List<Cocktail>>

    /**
     * Adds a cocktail to user's favorites collection.
     * @param cocktail The cocktail to add to favorites
     */
    suspend fun addToFavorites(cocktail: Cocktail)

    /**
     * Removes a cocktail from user's favorites collection.
     * @param cocktailId The ID of cocktail to remove from favorites
     */
    suspend fun removeFromFavorites(cocktailId: String)

    /**
     * Retrieves cocktails filtered by specific category.
     * @param category The category to filter cocktails by
     * @return Flow emitting list of cocktails in the category
     */
    suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>>
}
