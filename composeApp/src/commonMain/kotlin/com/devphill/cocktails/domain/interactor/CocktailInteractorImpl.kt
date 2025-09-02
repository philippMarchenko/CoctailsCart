package com.devphill.cocktails.domain.interactor

import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow

/**
 * Default implementation of CocktailInteractor.
 * Provides concrete business logic for cocktail operations by delegating to the repository layer.
 *
 * @param repository The cocktail repository for data access
 */
class CocktailInteractorImpl(
    private val repository: CocktailRepository
) : CocktailInteractor {

    /**
     * Retrieves all cocktails from the repository as a reactive stream.
     * @return Flow emitting list of all available cocktails
     */
    override suspend fun getAllCocktails(): Flow<List<Cocktail>> {
        return repository.getAllCocktails()
    }

    /**
     * Searches for cocktails matching the specified query string.
     * @param query The search term to match against cocktail names and ingredients
     * @return Flow emitting list of cocktails matching the search criteria
     */
    override suspend fun searchCocktails(query: String): Flow<List<Cocktail>> {
        return repository.searchCocktails(query)
    }

    /**
     * Retrieves all cocktails marked as favorites by the user.
     * @return Flow emitting list of favorite cocktails
     */
    override suspend fun getFavoriteCocktails(): Flow<List<Cocktail>> {
        return repository.getFavoriteCocktails()
    }

    /**
     * Finds a specific cocktail by its unique identifier.
     * @param id The unique cocktail identifier to search for
     * @return The cocktail if found, null if no cocktail matches the ID
     */
    override suspend fun getCocktailById(id: String): Cocktail? {
        return repository.getCocktailById(id)
    }

    /**
     * Retrieves cocktails filtered by the specified category.
     * @param category The category name to filter cocktails by
     * @return Flow emitting list of cocktails in the specified category
     */
    override suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> {
        return repository.getCocktailsByCategory(category)
    }

    /**
     * Toggles the favorite status of a cocktail based on current state.
     * Adds cocktail to favorites if not favorited, removes if already favorited.
     * @param cocktail The cocktail to toggle favorite status for
     * @param isFavorite Current favorite state - true if currently favorited, false otherwise
     */
    override suspend fun toggleFavorite(cocktail: Cocktail, isFavorite: Boolean) {
        if (isFavorite) {
            repository.removeFromFavorites(cocktail.id)
        } else {
            repository.addToFavorites(cocktail)
        }
    }
}
