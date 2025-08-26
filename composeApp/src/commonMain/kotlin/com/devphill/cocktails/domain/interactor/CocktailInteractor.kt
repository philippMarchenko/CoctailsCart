package com.devphill.cocktails.domain.interactor

import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow

/**
 * Interactor that handles all cocktail-related business logic operations.
 *
 * This interactor acts as an intermediary between ViewModels and the Repository,
 * providing a clean interface for all cocktail operations while encapsulating
 * business logic and data transformations.
 *
 * ## Architecture Flow
 * ViewModel → CocktailInteractor → CocktailRepository → DataSources
 *
 * @param repository The cocktail repository for data access
 */
class CocktailInteractor(
    private val repository: CocktailRepository
) {
    /**
     * Retrieves all cocktails as a reactive data stream.
     *
     * @return Flow of all cocktails from the repository
     */
    suspend fun getAllCocktails(): Flow<List<Cocktail>> {
        return repository.getAllCocktails()
    }

    /**
     * Searches for cocktails matching the given query.
     *
     * @param query The search term to match against cocktail data
     * @return Flow of cocktails matching the search query
     */
    suspend fun searchCocktails(query: String): Flow<List<Cocktail>> {
        return repository.searchCocktails(query)
    }

    /**
     * Retrieves all favorite cocktails.
     *
     * @return Flow of cocktails marked as favorites
     */
    suspend fun getFavoriteCocktails(): Flow<List<Cocktail>> {
        return repository.getFavoriteCocktails()
    }

    /**
     * Retrieves a specific cocktail by its unique identifier.
     *
     * @param id The unique identifier of the cocktail
     * @return The cocktail if found, null otherwise
     */
    suspend fun getCocktailById(id: String): Cocktail? {
        return repository.getCocktailById(id)
    }

    /**
     * Retrieves cocktails filtered by category.
     *
     * @param category The category to filter by
     * @return Flow of cocktails in the specified category
     */
    suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> {
        return repository.getCocktailsByCategory(category)
    }

    /**
     * Toggles the favorite status of a cocktail.
     *
     * This method handles the business logic of determining whether to add or remove
     * a cocktail from favorites based on its current state.
     *
     * @param cocktail The cocktail to toggle favorite status for
     * @param isFavorite Current favorite status (true if currently favorite)
     */
    suspend fun toggleFavorite(cocktail: Cocktail, isFavorite: Boolean) {
        if (isFavorite) {
            repository.removeFromFavorites(cocktail.id)
        } else {
            repository.addToFavorites(cocktail)
        }
    }

    /**
     * Adds a cocktail to favorites.
     *
     * @param cocktail The cocktail to mark as favorite
     */
    suspend fun addToFavorites(cocktail: Cocktail) {
        repository.addToFavorites(cocktail)
    }

    /**
     * Removes a cocktail from favorites.
     *
     * @param cocktailId The ID of the cocktail to remove from favorites
     */
    suspend fun removeFromFavorites(cocktailId: String) {
        repository.removeFromFavorites(cocktailId)
    }
}
